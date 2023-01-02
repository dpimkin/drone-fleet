package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.domain.MedicationPayload;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class LoadDroneDTO {

    String droneSerialNumber;

    int requiredWeight;

    List<MedicationPayload> payloadList;
}
