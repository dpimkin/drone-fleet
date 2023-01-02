package com.musalasoft.dronefleet.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveDroneRepository extends ReactiveCrudRepository<DroneEntity, Long> {

//    Flux<DroneDocument> findByDeleted(boolean deleted);
//
//    Mono<DroneDocument> findByIdAndDeleted(String id, boolean deleted);
//
//    Mono<DroneDocument> findBySerialNumberAndDeleted(String serialNumber, boolean deleted);

}
