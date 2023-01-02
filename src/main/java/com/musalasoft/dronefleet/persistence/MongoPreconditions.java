package com.musalasoft.dronefleet.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Controller;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Controller
@RequiredArgsConstructor
public class MongoPreconditions implements ApplicationListener<ApplicationReadyEvent> {
    private final ReactiveMongoOperations mongoOps;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        mongoOps.indexOps(DroneDocument.class)
                .ensureIndex(new Index().named("sn_unique")
                        .on("sn", ASC)
                        .unique())
                .block();
    }


}
