package com.musalasoft.dronefleet.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MedicationPayloadRepository extends ReactiveCrudRepository<MedicationPayloadEntity, Long> {

    Flux<MedicationPayloadEntity> findByDroneRef(long droneRef);

}
