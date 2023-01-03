package dpimkin.drone.fleet.boundary;

import dpimkin.drone.fleet.domain.MedicationPayload;
import dpimkin.drone.fleet.persistence.MedicationPayloadEntity;
import org.mapstruct.Mapper;

@Mapper
public interface MedicationMapper {
    MedicationPayload mapMedicationPayloadEntity(MedicationPayloadEntity entity);

    default MedicationPayloadEntity mapMedicationPayload(Long droneRef, MedicationPayload payload) {
        return new MedicationPayloadEntity(null,
                droneRef,
                payload.getCode(),
                payload.getName(),
                payload.getWeight(),
                payload.getQty(),
                payload.getImage());
    }
}
