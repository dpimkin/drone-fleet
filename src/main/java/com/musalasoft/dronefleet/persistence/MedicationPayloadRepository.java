package com.musalasoft.dronefleet.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationPayloadRepository extends ReactiveCrudRepository<MedicationPayloadEntity, Long> {

}
