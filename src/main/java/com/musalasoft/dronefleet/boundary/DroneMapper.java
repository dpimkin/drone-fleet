package com.musalasoft.dronefleet.boundary;

import com.musalasoft.dronefleet.domain.DroneDTO;
import com.musalasoft.dronefleet.persistence.DroneEntity;
import org.mapstruct.Mapper;

@Mapper
public interface DroneMapper {

    default DroneDTO mapDroneEntity(DroneEntity doc) {
        return new DroneDTO()
                .setId(doc.id())
                .setSerialNumber(doc.serialNumber())
                .setBatteryCapacity(doc.batteryCapacity())
                .setWeightCapacity(doc.weightCapacity())
                .setWeightLimit(doc.weightLimit())
                .setModelType(doc.type())
                .setState(doc.state());

    }
}
