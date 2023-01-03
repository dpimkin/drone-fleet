package dpimkin.drone.fleet.service;

import dpimkin.drone.fleet.domain.MedicationPayload;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class LoadDroneDTO {
    private String serialNumber;
    private String idempotencyKey;
    private int requiredWeight;
    private List<MedicationPayload> payloadList;
}
