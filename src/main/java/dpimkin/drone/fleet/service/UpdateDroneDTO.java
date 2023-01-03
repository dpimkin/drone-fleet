package dpimkin.drone.fleet.service;

import dpimkin.drone.fleet.domain.DroneState;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateDroneDTO {
    private String serialNumber;
    private String idempotencyKey;
    private DroneState state;
    private Integer batteryCapacity;

}
