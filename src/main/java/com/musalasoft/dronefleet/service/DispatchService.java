package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.persistence.DroneEntity;
import com.musalasoft.dronefleet.persistence.DroneRepository;
import com.musalasoft.dronefleet.persistence.IdempotentOperationEntity;
import com.musalasoft.dronefleet.persistence.MedicationPayloadEntity;
import com.musalasoft.dronefleet.persistence.MedicationPayloadRepository;
import com.musalasoft.dronefleet.service.OperationLogService.GenericIdempotentOperationContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchService {
    private final DroneRepository droneRepository;
    private final MedicationPayloadRepository medicationPayloadRepository;
    private final OperationLogService operationLogService;
    private final Settings settings;

    @Transactional(readOnly = true)
    public Flux<DroneEntity> findAvailableDrones(int limit) {
        return droneRepository.findAvailableDrones(settings.lowBatteryThreshold, limit);
    }

    @Transactional(readOnly = true)
    public Flux<MedicationPayloadEntity> findMedicationPayloadByDroneSn(String droneSerialNumber) {
        return droneRepository.findBySerialNumber(droneSerialNumber)
                .flatMapMany(droneEntity -> medicationPayloadRepository.findByDroneRef(droneEntity.id()));
    }




    static class LoadMedicationContext extends GenericIdempotentOperationContent {

        LoadMedicationContext(IdempotentOperationEntity value) {
            super(value);
        }
    }

}
