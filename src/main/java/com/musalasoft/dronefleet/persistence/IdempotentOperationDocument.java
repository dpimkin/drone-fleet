package com.musalasoft.dronefleet.persistence;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@Accessors(chain = true)
@Document(collection = "idempotent_operation")
public class IdempotentOperationDocument {
    public static final String IDEMPOTENCY_KEY_FIELD = "idempotencyKey";

    @Id
    private String id;

    @Field("idempotency_key")
    private String idempotencyKey;

    private Instant created;

    private Integer result;
}
