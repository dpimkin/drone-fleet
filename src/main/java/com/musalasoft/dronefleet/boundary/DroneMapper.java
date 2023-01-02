package com.musalasoft.dronefleet.boundary;

import com.musalasoft.dronefleet.domain.DroneDTO;
import com.musalasoft.dronefleet.persistence.DroneEntity;
import org.mapstruct.Mapper;

@Mapper
public interface DroneMapper {

    DroneDTO mapDroneDocument(DroneEntity doc);

}
