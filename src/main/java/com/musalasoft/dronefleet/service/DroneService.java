package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.boundary.DroneMapper;
import com.musalasoft.dronefleet.domain.DroneDTO;
import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import com.musalasoft.dronefleet.persistence.DroneEntity;

import com.musalasoft.dronefleet.persistence.IdempotentOperationRepository;
import com.musalasoft.dronefleet.persistence.ReactiveDroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.time.Instant.now;


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

    public Mono<Integer> updateDroneBySerialNumber(UpdateDroneRequestBySerialNumberDTO request) {
        return null;
    }

    @Transactional
    public Mono<Integer> updateDroneById(UpdateDroneRequestByIdDTO request) {
        return null;
    }

    public Flux<DroneDTO> findAll(int limit) {
        return Flux.empty();
        //return droneRepository.findByDeleted(false).take(limit).map(mapper::mapDroneDocument);
    }




//    Mono<IdempotentOperationDocument> newIdempotentOperation(String idempotencyKey) {
//        return idempotentOperationRepository.save(new IdempotentOperationDocument()
//                .setIdempotencyKey(idempotencyKey)
//                .setCreated(now()));
//
//    }

//    private Mono<DroneDocument> updateDroneDocument(DroneDocument drone, GenericDroneRequestDTO request) {
//        ofNullable(request.getBatteryCapacity()).ifPresent(drone::setBatteryCapacity);
//
//        if (null != request.getState()) {
//            if (request.getState() == LOADING && (drone.getState() != LOADING)) {
//                throw new LowBatteryException()
//            }
//        }
//        return drone;
//    }




}
