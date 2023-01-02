package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import com.musalasoft.dronefleet.persistence.ReactiveDroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class DroneService {
    private final ReactiveDroneRepository dao;

    Mono<Void> registerDrone(RegisterDroneRequestDTO request) {
        return Mono.just("test").then();
    }

}
