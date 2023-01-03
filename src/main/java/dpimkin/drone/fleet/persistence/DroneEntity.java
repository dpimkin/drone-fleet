package dpimkin.drone.fleet.persistence;


import dpimkin.drone.fleet.domain.DroneModelType;
import dpimkin.drone.fleet.domain.DroneState;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("drone")
public record DroneEntity(@Id Long id,
                          @Column("sn") String serialNumber,
                          @Column("drone_type") DroneModelType type,
                          @Column("drone_state") DroneState state,
                          @Column("battery_cap") Integer batteryCapacity,
                          @Column("weight_cap") Integer weightCapacity,
                          @Column("weight_max") Integer weightLimit) {
}
