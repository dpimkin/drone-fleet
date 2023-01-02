package com.musalasoft.dronefleet.persistence;

import com.musalasoft.dronefleet.DockerizedMongoSupport;
import com.musalasoft.dronefleet.service.DroneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.time.Instant;

import static java.time.Instant.now;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = "logging.level.org.mongodb.driver=TRACE")
class MongoPreconditionsTest extends DockerizedMongoSupport {
    private static final String SAME_SERIAL_NUMBER ="1101";
    private static final String SAME_IDEMPOTENCY_KEY = "foobar";

    @Autowired
    ReactiveDroneRepository droneRepository;

    @Autowired
    IdempotentOperationRepository idempotentOperationRepository;

    @Autowired
    DroneService droneService;

    @Test
    void droneSerialNumber_shouldBeUnique() {
        StepVerifier.create(droneRepository.save(new DroneDocument()
                                .setSerialNumber(SAME_SERIAL_NUMBER))
                        .flatMap(doc -> droneRepository.save(new DroneDocument()
                                .setSerialNumber(SAME_SERIAL_NUMBER))))
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    void idempotencyKey_shouldBeUnique() {
        StepVerifier.create(idempotentOperationRepository.save(new IdempotentOperationDocument()
                                .setIdempotencyKey(SAME_IDEMPOTENCY_KEY)
                                .setCreated(now()))
                        .flatMap(doc -> idempotentOperationRepository.save((new IdempotentOperationDocument()
                                .setIdempotencyKey(SAME_IDEMPOTENCY_KEY)))))
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    void todo() {
        try {
            var l = droneService.test("QWERT").block();

            System.out.println(l.getId());

        } finally {
            var r = droneRepository.findBySerialNumberAndDeleted("QWERT", false).block();
            System.out.println("found " + r.getId());
        }




    }
}