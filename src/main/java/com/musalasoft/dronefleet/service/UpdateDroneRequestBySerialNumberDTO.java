package com.musalasoft.dronefleet.service;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateDroneRequestBySerialNumberDTO extends GenericDroneRequestDTO {
    String serialNumber;
}
