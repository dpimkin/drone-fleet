package dpimkin.drone.fleet.boundary;

import dpimkin.drone.fleet.domain.DroneDTO;
import dpimkin.drone.fleet.persistence.DroneEntity;
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
