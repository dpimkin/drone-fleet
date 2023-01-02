package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.boundary.DroneMapper;
import com.musalasoft.dronefleet.domain.DroneDTO;
import com.musalasoft.dronefleet.domain.DroneState;
import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import com.musalasoft.dronefleet.persistence.DroneEntity;
import com.musalasoft.dronefleet.persistence.IdempotentOperationEntity;
import com.musalasoft.dronefleet.persistence.IdempotentOperationRepository;
import com.musalasoft.dronefleet.persistence.ReactiveDroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Function;

import static java.time.Instant.now;
import static java.util.Optional.ofNullable;


@Slf4j
@Service
@RequiredArgsConstructor
public class DroneService {
    private final Settings settings;
    private final ReactiveDroneRepository droneRepository;
    private final IdempotentOperationRepository idempotentOperationRepository;


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
    public Mono<DroneEntity> findDroneById(Long id) {
        log.info("fetching drone by id '{}'", id);
        return droneRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Mono<DroneEntity> findDroneBySerialNumber(String sn) {
        log.info("fetching drone by s/n '{}'", sn);
        return droneRepository.findBySerialNumber(sn);//.map(mapper::mapDroneEntity);

    }

    @Transactional
    public Mono<IdempotentOperationEntity> updateDroneById(UpdateDroneRequestByIdDTO request) {
        return updateDrone(request, (idempotency) -> droneRepository.findById(request.getId()));
    }

    @Transactional
    public Mono<IdempotentOperationEntity> updateDroneBySerialNumber(UpdateDroneRequestBySerialNumberDTO request) {
        return updateDrone(request, (idempotency) -> droneRepository.findBySerialNumber(request.getSerialNumber()));
    }


    /**
     * write through idempotent operation log to ensure
     */
    private Mono<IdempotentOperationEntity> updateDrone(GenericDroneRequestDTO request, Function<IdempotentOperationEntity, Mono<DroneEntity>> fetchFn) {


        return idempotentOperationRepository.save(new IdempotentOperationEntity(null, request.getIdempotencyKey(), 0, now()))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    var message = "stalled operation with idempotency-key " + request.getIdempotencyKey();
                    log.warn(message);
                    return Mono.error(new StalledUpdateException(message));
                })
                .flatMap(fetchFn)
                .flatMap(entity -> mergeDroneEntity(request, entity))
                .flatMap(entity -> idempotentOperationRepository.findByIdempotencyKey(request.getIdempotencyKey()))
                .flatMap(entity -> idempotentOperationRepository.save(new IdempotentOperationEntity(entity.id(), entity.idempotencyKey(), HttpStatus.OK.ordinal(), entity.created())))
    }

    public Flux<DroneDTO> findAll(int limit) { // TODO
        return Flux.empty();
    }


    private Mono<DroneEntity> mergeDroneEntity(GenericDroneRequestDTO request, DroneEntity entity) {
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
}
