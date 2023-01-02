package com.musalasoft.dronefleet.persistence;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Accessors(chain = true)
public class DroneDocument {

    @Id
    private String id;

    private String serialNumber;

}
