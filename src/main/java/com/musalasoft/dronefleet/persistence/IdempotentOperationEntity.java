package com.musalasoft.dronefleet.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;


@Table("operation_log")
public record IdempotentOperationEntity(@Id Long id,
                                        @Column("idempotency_key") String idempotencyKey,
                                        @Column("status") int status,
                                        @Column("created")Instant created) {
}

