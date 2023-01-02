package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.domain.DroneState;
import lombok.Data;
import lombok.Value;

@Data
public class UpdateDroneRequestByIdDTO extends GenericDroneRequestDTO {
    String id;
}
