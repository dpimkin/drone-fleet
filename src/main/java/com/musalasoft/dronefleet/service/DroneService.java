package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import com.musalasoft.dronefleet.persistence.DroneDocument;
import com.musalasoft.dronefleet.persistence.ReactiveDroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.musalasoft.dronefleet.persistence.DroneDocument.INITIAL_VERSION;


@Slf4j
@Service
@RequiredArgsConstructor
public class DroneService {
    private final ReactiveDroneRepository dao;

    Mono<DroneDocument> registerDrone(RegisterDroneRequestDTO request) {
        var drone = new DroneDocument()
                .setVersion(INITIAL_VERSION)
                .setDeleted(false)
                .setSerialNumber(request.getSerialNumber())
                .setModelType(request.getModelType())
                .setState(request.getState())
                .setBatteryCapacity(request.getBatteryCapacity())
                .setWeightCapacity(request.getWeightLimit())
                .setWeightLimit(request.getWeightLimit());

        return dao.save(drone);
    }

}
