package com.musalasoft.dronefleet.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DronePayloadDTO {

    @NotNull
    @Valid
    List<MedicationPayloadDTO> payloadList;
}
