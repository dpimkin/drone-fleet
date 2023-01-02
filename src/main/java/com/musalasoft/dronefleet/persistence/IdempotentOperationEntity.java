package com.musalasoft.dronefleet.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("idempotent_operation")
public record IdempotentOperationEntity(@Id Long id, @Column("idempotency_key") String idempotencyKey) {
}

