package dpimkin.drone.fleet.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("medication_payload")
public record MedicationPayloadEntity(@Id Long id,
                                      @Column("drone_id") Long droneRef,
                                      @Column String code,
                                      @Column String name,
                                      @Column Integer weight,
                                      @Column Integer qty,
                                      @Column("image") String image) {
}
