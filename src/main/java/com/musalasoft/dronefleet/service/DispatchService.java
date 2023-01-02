package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.persistence.DroneRepository;
import com.musalasoft.dronefleet.persistence.MedicationPayloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchService {
    private final DroneRepository droneRepository;
    private final MedicationPayloadRepository medicationPayloadRepository;
    private final OperationLogService operationLogService;
}
