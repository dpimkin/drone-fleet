package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.domain.DroneState;
import lombok.Data;

@Data
public class GenericDroneRequestDTO {
    private String idempotencyKey;
    private DroneState state;
    private Integer batteryCapacity;
}
