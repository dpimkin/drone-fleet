package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.persistence.DroneEntity;
import com.musalasoft.dronefleet.persistence.DroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {
    private final DroneRepository dao;
    private final Settings settings;

    @Transactional(readOnly = true)
    public Flux<DroneEntity> findDronesWithLowBattery() {
        return dao.findDronesWithLowBattery(settings.lowBatteryThreshold);
    }
}
