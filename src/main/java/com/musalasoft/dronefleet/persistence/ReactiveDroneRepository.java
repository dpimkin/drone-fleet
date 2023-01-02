package com.musalasoft.dronefleet.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveDroneRepository extends ReactiveCrudRepository<DroneEntity, Long> {

    Mono<DroneEntity> findBySerialNumber(String serialNumber);


}
