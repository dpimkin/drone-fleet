package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.boundary.DroneMapper;
import com.musalasoft.dronefleet.domain.DroneState;
import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import com.musalasoft.dronefleet.persistence.DroneEntity;
import com.musalasoft.dronefleet.persistence.IdempotentOperationEntity;
import com.musalasoft.dronefleet.persistence.ReactiveDroneRepository;
import com.musalasoft.dronefleet.service.OperationLogService.GenericIdempotentOperationContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@Service
@RequiredArgsConstructor
public class DroneService {
    private final Settings settings;
    private final ReactiveDroneRepository droneRepository;


    private final OperationLogService operationLogService;

    private final DroneMapper mapper;

    @Transactional
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
    public Mono<DroneEntity> findDroneBySn(String sn) {
        log.info("fetching drone by s/n '{}'", sn);
        return droneRepository.findBySerialNumber(sn);
    }


    /**
     * write through idempotent operation log to ensure
     */
    @Transactional
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
                var message = "Battery is too low for loading drone with id " + entity.id() +
                        ". Wait until " + settings.lowBatteryThreshold + "%";
                log.error(message);
                return Mono.error(new LowBatteryException(message));
            }
        }
        return droneRepository.save(merged);
    }


    static class UpdateOperationContext extends GenericIdempotentOperationContent {
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
