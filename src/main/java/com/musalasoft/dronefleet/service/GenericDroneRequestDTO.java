package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.domain.DroneState;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenericDroneRequestDTO {
    private String idempotencyKey;
    private DroneState state;
    private Integer batteryCapacity;
}
