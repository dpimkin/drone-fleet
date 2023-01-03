package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.domain.MedicationPayload;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class LoadDroneDTO {
    private String serialNumber;
    private String idempotencyKey;
    private int requiredWeight;
    private List<MedicationPayload> payloadList;
}
