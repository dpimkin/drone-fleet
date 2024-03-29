package dpimkin.drone.fleet.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DronePayloadDTO {

    @NotNull
    @Valid
    @NotEmpty
    List<MedicationPayload> payloadList;
}
