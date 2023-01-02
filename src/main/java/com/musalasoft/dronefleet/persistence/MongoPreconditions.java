package com.musalasoft.dronefleet.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MongoPreconditions implements ApplicationListener<ApplicationReadyEvent> {
    private final ReactiveMongoOperations mongoOps;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ensureUniqueDroneSerialNumber()
                .flatMap(s -> ensureUniqueIdempotencyKey())
                .block();
//        var s = mongoOps.indexOps(DroneDocument.class)
//                .ensureIndex(new Index().named("sn_unique")
//                        .on(DroneDocument.SN_FIELD, ASC)
//                        .unique())
//                .flatMap((s) ->
//                        mongoOps.indexOps(IdempotentOperationDocument.class)
//                                .ensureIndex(new Index().named("idempotency_key_unique")
//                                        .on(IdempotentOperationDocument.IDEMPOTENCY_KEY_FIELD, ASC)
//                                        .unique()))
//                .block();
    }

    Mono<String> ensureUniqueDroneSerialNumber() {
        log.info("ensureing unique drone serial number");
        return mongoOps.indexOps(DroneDocument.class)
                .ensureIndex(new Index().named("sn_unique")
                        .on(DroneDocument.SN_FIELD, ASC)
                        .unique());
    }

    Mono<String> ensureUniqueIdempotencyKey() {
        log.info("ensureing unique idempotency key");
        return mongoOps.indexOps(IdempotentOperationDocument.class)
                .ensureIndex(new Index().named("idempotency_key_unique")
                        .on(IdempotentOperationDocument.IDEMPOTENCY_KEY_FIELD, ASC)
                        .unique());
    }




}
