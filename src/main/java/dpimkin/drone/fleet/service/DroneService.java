package dpimkin.drone.fleet.service;

import dpimkin.drone.fleet.domain.DroneState;
import dpimkin.drone.fleet.domain.RegisterDroneRequestDTO;
import dpimkin.drone.fleet.persistence.DroneEntity;
import dpimkin.drone.fleet.persistence.DroneRepository;
import dpimkin.drone.fleet.persistence.IdempotentOperationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;


@Slf4j
@Service
@RequiredArgsConstructor
public class DroneService {
    private final Settings settings;
    private final DroneRepository droneRepository;
    private final OperationLogService operationLogService;


    @Transactional
    @Cacheable(cacheNames = {"drone-cache"}, key = "{#request.getSerialNumber()}")
    public Mono<DroneEntity> registerDrone(RegisterDroneRequestDTO request) {
        log.info("registering drone with s/n: '{}'", request.getSerialNumber());
        return droneRepository.save(new DroneEntity(null,
                request.getSerialNumber(),
                request.getModelType(),
                request.getState(),
                request.getBatteryCapacity(),
                request.getWeightLimit(),
                request.getWeightLimit()));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = {"drone-cache"}, key = "{#sn}")
    public Mono<DroneEntity> findDroneBySn(String sn) {
        log.info("fetching drone by s/n '{}'", sn);
        return droneRepository.findBySerialNumber(sn);
    }

    @Transactional(readOnly = true)
    public Flux<DroneEntity> findDrones(int limit) {
        return droneRepository.findAll()
                .take(limit);
    }


    /**
     * write through idempotent operation log to ensure
     */
    @Transactional(propagation = REQUIRES_NEW)
    @CacheEvict(cacheNames = {"drone-cache"}, key = "{#request.getSerialNumber()}")
    public Mono<Integer> updateDroneBySerialNumber(UpdateDroneDTO request) {
        return operationLogService.newIdempotentOperation(request.getIdempotencyKey())
                .map(UpdateOperationContext::new)
                .flatMap(context -> droneRepository.findBySerialNumber(request.getSerialNumber())
                        .map(context::setDroneEntity))
                .flatMap(context -> mergeDroneEntity(request, context.getDroneEntity())
                        .map(entity -> context))
                .flatMap(context -> operationLogService.mergeIdempotentOperation(context.getIdempotentOperationEntity(), OK.value())
                        .map(IdempotentOperationEntity::status));
    }


    private Mono<DroneEntity> mergeDroneEntity(UpdateDroneDTO request, DroneEntity entity) {
        var merged = new DroneEntity(entity.id(),
                entity.serialNumber(),
                entity.type(),
                ofNullable(request.getState()).orElse(entity.state()),
                ofNullable(request.getBatteryCapacity()).orElse(entity.batteryCapacity()),
                entity.weightCapacity(),
                entity.weightLimit());

        if (merged.batteryCapacity() < settings.lowBatteryThreshold) {
            if (request.getState() == DroneState.LOADING) {
                var message = "Battery is too low for loading drone with s/n " + entity.serialNumber() +
                        ". Wait at least " + settings.lowBatteryThreshold + "% drone battery";
                log.error(message);
                return Mono.error(new LowBatteryException(message));
            }
        }
        return droneRepository.save(merged);
    }


    static class UpdateOperationContext extends OperationLogService.GenericIdempotentOperationContent {
        final AtomicReference<DroneEntity> droneEntityRef = new AtomicReference<>();

        UpdateOperationContext(IdempotentOperationEntity value) {
            super(value);
        }

        public DroneEntity getDroneEntity() {
            return droneEntityRef.get();
        }

        public UpdateOperationContext setDroneEntity(DroneEntity value) {
            droneEntityRef.set(value);
            return this;
        }
    }
}
