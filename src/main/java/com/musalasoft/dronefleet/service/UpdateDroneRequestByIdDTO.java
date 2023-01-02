package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.domain.DroneState;
import lombok.Data;
import lombok.Value;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateDroneRequestByIdDTO extends GenericDroneRequestDTO {
    Long id;
}
