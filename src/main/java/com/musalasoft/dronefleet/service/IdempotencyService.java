package com.musalasoft.dronefleet.service;

import com.musalasoft.dronefleet.persistence.IdempotentOperationEntity;
import com.musalasoft.dronefleet.persistence.IdempotentOperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static java.time.Instant.now;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final IdempotentOperationRepository idempotentOperationRepository;


    @Transactional
    Mono<IdempotentOperationEntity> newIdempotentOperation(String idempotencyKey) {
        return idempotentOperationRepository.findByIdempotencyKey(idempotencyKey);
    }

    @Transactional(propagation = REQUIRES_NEW, isolation = READ_COMMITTED)
    Mono<IdempotentOperationEntity> updateIdempotentOperationStatus(String idempotencyKey, int status) {

//        idempotentOperationRepository.findByIdempotencyKey(idempotencyKey)
//                .flatMap(entity -> {
//                    var merged = new IdempotentOperationEntity()
//                })


        return null;


    }


}
