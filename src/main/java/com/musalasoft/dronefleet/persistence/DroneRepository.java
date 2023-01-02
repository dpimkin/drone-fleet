package com.musalasoft.dronefleet.persistence;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DroneRepository extends ReactiveCrudRepository<DroneEntity, Long> {

    Mono<DroneEntity> findBySerialNumber(String serialNumber);


    @Query("""
            SELECT * FROM drone
            WHERE drone_state = 'IDLE' OR drone_state = 'LOADING'
            LIMIT $1             
            """)
    Flux<DroneEntity> findAvailableDrones(int limit);


}
