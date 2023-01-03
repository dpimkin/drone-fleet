package dpimkin.drone.fleet.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateDroneRequestDTO {
    DroneState state;
    Integer batteryCapacity;
}
