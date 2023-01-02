package com.musalasoft.dronefleet.persistence;

import com.musalasoft.dronefleet.domain.DroneModelType;
import com.musalasoft.dronefleet.domain.DroneState;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document
@Accessors(chain = true)
public class DroneDocument {
    public static final long INITIAL_VERSION = 1;

    @Id
    private String id;

    @Version
    private Long version;

    private DroneModelType modelType;

    @Field("battery")
    Integer batteryCapacity;

    private DroneState state;

    @Field("sn")
    private String serialNumber;

    private int weightCapacity;

    private int weightLimit;

    private Boolean deleted;
}
