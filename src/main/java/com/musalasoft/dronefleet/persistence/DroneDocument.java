package com.musalasoft.dronefleet.persistence;

//import com.musalasoft.dronefleet.domain.DroneModelType;
//import com.musalasoft.dronefleet.domain.DroneState;
//import com.musalasoft.dronefleet.domain.MedicationPayload;
//import lombok.Data;
//import lombok.experimental.Accessors;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.Version;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//
//import java.util.List;
//
//@Data
//@Document(collection = "drone")
//@Accessors(chain = true)
//public class DroneDocument {
//    public static final String SN_FIELD = "sn";
//
//    @Id
//    private String id;
//
//    @Version
//    private long version;
//
//    private Boolean deleted;
//
//    @Field("type")
//    private DroneModelType modelType;
//
//    @Field("battery")
//    Integer batteryCapacity;
//
//    private DroneState state;
//
//    @Field(SN_FIELD)
//    private String serialNumber;
//
//    private int weightCapacity;
//
//    private int weightLimit;
//
//    private List<MedicationPayload> payloadList;
//}
