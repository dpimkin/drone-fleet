package com.musalasoft.dronefleet.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DroneDTO {
    String id;
    String serialNumber;
    DroneModelType modelType;
    int weightCapacity;
    int weightLimit;
    DroneState state;
}
