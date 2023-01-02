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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.musalasoft.dronefleet.domain.DroneState.IDLE;
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

    public Mono<DroneEntity> registerDrone(RegisterDroneRequestDTO request) {
        return droneRepository.save(new DroneEntity(null,
                request.getSerialNumber(),
                request.getModelType(),
                request.getState()));



        //return Mono.just(new DroneEntity(null, null)); // TODO implement it
//        var drone = new DroneDocument()
//                .setDeleted(false)
//                .setSerialNumber(request.getSerialNumber())
//                .setModelType(request.getModelType())
//                .setState(ofNullable(request.getState()).orElse(IDLE))
//                .setBatteryCapacity(request.getBatteryCapacity())
//                .setWeightCapacity(request.getWeightLimit())
//                .setWeightLimit(request.getWeightLimit());
//        return droneRepository.save(drone);
    }


//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public Mono<DroneEntity> test(String sn) {
//
//        return droneRepository.save(new DroneDocument()
//                .setDeleted(false)
//                .setSerialNumber(sn)).flatMap( r -> {
//            return idempotentOperationRepository.save(new IdempotentOperationDocument()
//                    .setIdempotencyKey("FOO")).map( unused -> r);
//        }).flatMap(r -> {
//            return idempotentOperationRepository.save(new IdempotentOperationDocument()
//                    .setIdempotencyKey("FOO")).map( unused -> r);
//        });
//
//
//
//    }

    public Mono<Integer> updateDroneBySerialNumber(UpdateDroneRequestBySerialNumberDTO request) {
        return null;
    }

    @Transactional
    public Mono<Integer> updateDroneById(UpdateDroneRequestByIdDTO request) {
        return null;
//        idempotentOperationRepository.findByIdempotencyKey(request.getIdempotencyKey())
//                .map(doc -> {
//                    if (null == doc.getResult()) {
//                        throw new ConcurrentIdempontencyException(request.getIdempotencyKey());
//                    }
//                    return doc.getResult();
//                })
//                .switchIfEmpty(Mono.defer(() -> newIdempotentOperation(request.getIdempotencyKey())
//                        .flatMap(doc -> droneRepository.findByIdAndDeleted(request.getId(), false)
//                                .flatMap(drone ->  ))

    }

    public Flux<DroneDTO> findAll(int limit) {
        return Flux.empty();
        //return droneRepository.findByDeleted(false).take(limit).map(mapper::mapDroneDocument);
    }

    public Mono<DroneDTO> findDroneById(String id) {
        return Mono.empty();
        //return droneRepository.findByIdAndDeleted(id, false).map(mapper::mapDroneDocument);
    }

    public Mono<DroneDTO> findDroneBySerialNumber(String sn) {
        return Mono.empty();
        //return droneRepository.findBySerialNumberAndDeleted(sn, false).map(mapper::mapDroneDocument);
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
