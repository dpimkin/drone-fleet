package com.musalasoft.dronefleet.persistence;


import com.musalasoft.dronefleet.domain.DroneModelType;
import com.musalasoft.dronefleet.domain.DroneState;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("drone")
public record DroneEntity(@Id Long id,
                          @Column("sn") String serialNumber,
                          @Column("drone_type") DroneModelType type,
                          @Column("drone_state") DroneState state) {
}
