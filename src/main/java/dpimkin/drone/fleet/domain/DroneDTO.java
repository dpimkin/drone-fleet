package dpimkin.drone.fleet.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DroneDTO {
    Long id;
    String serialNumber;
    DroneModelType modelType;
    Integer batteryCapacity;
    Integer weightCapacity;
    Integer weightLimit;
    DroneState state;
}
