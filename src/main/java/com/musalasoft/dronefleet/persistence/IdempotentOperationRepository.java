package com.musalasoft.dronefleet.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IdempotentOperationRepository extends ReactiveCrudRepository<IdempotentOperationEntity, Long> {
    Mono<IdempotentOperationEntity> findByIdempotencyKey(String idempotencyKey);
}
