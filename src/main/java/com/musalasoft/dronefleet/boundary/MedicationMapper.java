package com.musalasoft.dronefleet.boundary;

import com.musalasoft.dronefleet.domain.MedicationPayload;
import com.musalasoft.dronefleet.persistence.MedicationPayloadEntity;
import org.mapstruct.Mapper;

@Mapper
public interface MedicationMapper {
    MedicationPayload mapMedicationPayloadEntity(MedicationPayloadEntity entity);
}
