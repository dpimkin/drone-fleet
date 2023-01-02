package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.persistence.IdempotentOperationDocument;
import com.musalasoft.dronefleet.persistence.IdempotentOperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static java.time.Instant.now;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final IdempotentOperationRepository idempotentOperationRepository;


    @Transactional(propagation = REQUIRES_NEW)
    Mono<IdempotentOperationDocument> newIdempotentOperation(String idempotencyKey) {
        return null;
//        return idempotentOperationRepository.save(new IdempotentOperationDocument()
//                .setIdempotencyKey(idempotencyKey)
//                .setCreated(now()));
    }

    @Transactional(propagation = REQUIRES_NEW)
    Mono<IdempotentOperationDocument> updateIdempotentOperationStatus(String idempotencyKey, int status) {
        return null;


    }


}
