package dpimkin.drone.fleet.service;

import dpimkin.drone.fleet.persistence.IdempotentOperationEntity;
import dpimkin.drone.fleet.persistence.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static java.time.Instant.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogService {
    private final OperationLogRepository operationLogRepository;

    @Transactional(readOnly = true)
    public Mono<Integer> fetchOperationResult(String idempotencyKey) {
        return operationLogRepository.findByIdempotencyKey(idempotencyKey).map(IdempotentOperationEntity::status);
    }


    Mono<IdempotentOperationEntity> newIdempotentOperation(String idempotencyKey) {
        return operationLogRepository.save(new IdempotentOperationEntity(null, idempotencyKey, 0, now()))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    var message = "stalled operation with idempotency-key " + idempotencyKey;
                    log.warn(message);
                    return Mono.error(new StalledOperationException(message));
                });
    }

    Mono<IdempotentOperationEntity> mergeIdempotentOperation(IdempotentOperationEntity entity, int status) {
        return operationLogRepository.save(new IdempotentOperationEntity(entity.id(), entity.idempotencyKey(), status, entity.created()));
    }


    static class GenericIdempotentOperationContent {
        private final AtomicReference<IdempotentOperationEntity> idempotentOperationEntityRef = new AtomicReference<>();

        GenericIdempotentOperationContent(IdempotentOperationEntity value) {
            idempotentOperationEntityRef.set(value);
        }

        public final IdempotentOperationEntity getIdempotentOperationEntity() {
            return idempotentOperationEntityRef.get();
        }

        public final GenericIdempotentOperationContent setIdempotentOperationEntity(IdempotentOperationEntity value) {
            idempotentOperationEntityRef.set(value);
            return this;
        }
    }
}
