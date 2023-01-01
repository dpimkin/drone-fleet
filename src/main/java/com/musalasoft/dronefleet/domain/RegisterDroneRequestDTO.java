package com.musalasoft.dronefleet.domain;

import lombok.Data;

@Data
public class RegisterDroneRequestDTO {
    String serialNumber;
    DroneModelType modelType;
    int weightCapacity;
    int weightLimit;
    DroneState state;
}
