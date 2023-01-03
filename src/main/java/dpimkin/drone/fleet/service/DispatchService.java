package dpimkin.drone.fleet.service;

import dpimkin.drone.fleet.boundary.MedicationMapper;
import dpimkin.drone.fleet.persistence.DroneEntity;
import dpimkin.drone.fleet.persistence.DroneRepository;
import dpimkin.drone.fleet.persistence.IdempotentOperationEntity;
import dpimkin.drone.fleet.persistence.MedicationPayloadEntity;
import dpimkin.drone.fleet.persistence.MedicationPayloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static dpimkin.drone.fleet.domain.DroneState.IDLE;
import static dpimkin.drone.fleet.domain.DroneState.LOADING;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchService {
    private final DroneRepository droneRepository;
    private final MedicationPayloadRepository medicationPayloadRepository;
    private final OperationLogService operationLogService;
    private final Settings settings;

    private final MedicationMapper medicationMapper;

    @Transactional(readOnly = true)
    public Flux<DroneEntity> findAvailableDrones(int limit) {
        return droneRepository.findAvailableDrones(settings.lowBatteryThreshold, limit);
    }

    @Transactional(readOnly = true)
    public Flux<MedicationPayloadEntity> findMedicationPayloadByDroneSn(String droneSerialNumber) {
        return droneRepository.findBySerialNumber(droneSerialNumber)
                .flatMapMany(droneEntity -> medicationPayloadRepository.findByDroneRef(droneEntity.id()));
    }

    @Transactional(propagation = REQUIRES_NEW)
    @CacheEvict(cacheNames = {"drone-cache"}, key = "{#request.getSerialNumber()}")
    public Mono<Integer> loadDrone(LoadDroneDTO request) {
        return operationLogService.newIdempotentOperation(request.getIdempotencyKey())
                .map(LoadMedicationOperationContext::new)
                .flatMap(context -> droneRepository.lockDroneBySerialNumberForLoading(request.getSerialNumber())
                        .map(context::setDroneEntity))
                .flatMap(context -> mergeDroneEntity(request, context.getDroneEntity())
                        .map(entity -> context))
                .flatMapMany(context -> medicationPayloadRepository.saveAll(request.getPayloadList()
                                .stream()
                                .map(dto -> medicationMapper.mapMedicationPayload(context.getDroneEntity().id(), dto))
                                .toList())
                        .last().map(entity -> context))
                .last()
                .flatMap(context -> operationLogService.mergeIdempotentOperation(context.getIdempotentOperationEntity(), OK.value())
                        .map(IdempotentOperationEntity::status));
    }



    private Mono<DroneEntity> mergeDroneEntity(LoadDroneDTO request, DroneEntity entity) {
        if (entity.state() != IDLE && entity.state() != LOADING) {
            var message = "Allowed only idle and loading drones. Drone s/n " + request.getSerialNumber()
                    + " is " + entity.state();
            log.error(message);
            return Mono.error(new IllegalDroneStateException(message));
        }

        if (entity.batteryCapacity() < settings.lowBatteryThreshold) {
            var message = "Battery is too low for loading drone with s/n " + entity.serialNumber() +
                    ". Wait at least " + settings.lowBatteryThreshold + "% drone battery";
            log.error(message);
            return Mono.error(new LowBatteryException(message));
        }

        if (entity.weightCapacity() - request.getRequiredWeight() < 0) {
            var message = "Not enough weight capacity for load drone s/n " + entity.serialNumber() +
                    "Available capacity " + entity.weightCapacity()+ "g, but offered "
                    + request.getRequiredWeight() + "g.";
            log.error(message);
            return Mono.error(new NotEnoughWeightCapacityException(message));
        }

        var merged = new DroneEntity(entity.id(),
                entity.serialNumber(),
                entity.type(),
                LOADING,
                entity.batteryCapacity(),
                entity.weightCapacity() - request.getRequiredWeight(),
                entity.weightLimit());

        return droneRepository.save(merged);
    }

    static class LoadMedicationOperationContext extends OperationLogService.GenericIdempotentOperationContent {
        final AtomicReference<DroneEntity> droneEntityRef = new AtomicReference<>();

        LoadMedicationOperationContext(IdempotentOperationEntity value) {
            super(value);
        }

        public DroneEntity getDroneEntity() {
            return droneEntityRef.get();
        }

        public LoadMedicationOperationContext setDroneEntity(DroneEntity value) {
            droneEntityRef.set(value);
            return this;
        }
    }
}
