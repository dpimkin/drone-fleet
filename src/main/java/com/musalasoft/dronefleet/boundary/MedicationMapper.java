package com.musalasoft.dronefleet.boundary;

import com.musalasoft.dronefleet.domain.MedicationPayload;
import com.musalasoft.dronefleet.persistence.MedicationPayloadEntity;
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
