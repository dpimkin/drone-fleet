package com.musalasoft.dronefleet.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReactiveDroneRepository extends ReactiveCrudRepository<DroneDocument, String> {
}
