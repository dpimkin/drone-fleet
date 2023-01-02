package com.musalasoft.dronefleet.persistence;

import com.musalasoft.dronefleet.DockerizedTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import static com.musalasoft.dronefleet.domain.DroneModelType.HEAVYWEIGHT;
import static com.musalasoft.dronefleet.domain.DroneModelType.LIGHTWEIGHT;
import static com.musalasoft.dronefleet.domain.DroneState.IDLE;
import static com.musalasoft.dronefleet.domain.DroneState.LOADING;
import static java.time.Instant.now;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {
        "logging.level.io.r2dbc.postgresql.QUERY=DEBUG",
        "logging.level.io.r2dbc.postgresql.PARAM=DEBUG"})
class PreconditionsTest extends DockerizedTestSupport {
    private static final String SAME_SERIAL_NUMBER ="1101";
    private static final String SAME_IDEMPOTENCY_KEY = "foobar";

    @Autowired
    ReactiveDroneRepository droneRepository;

    @Autowired
    IdempotentOperationRepository idempotentOperationRepository;

    @Test
    void droneSerialNumber_shouldBeUnique() {
        StepVerifier.create(droneRepository.save(new DroneEntity(null, SAME_SERIAL_NUMBER, LIGHTWEIGHT, IDLE))
                        .flatMap(entity -> droneRepository.save(
                                new DroneEntity(null, SAME_SERIAL_NUMBER, HEAVYWEIGHT, LOADING))))
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    void idempotencyKey_shouldBeUnique() {
        StepVerifier.create(idempotentOperationRepository.save(
                new IdempotentOperationEntity(null, SAME_IDEMPOTENCY_KEY, 0, now()))
                        .flatMap(entity -> idempotentOperationRepository.save(
                                new IdempotentOperationEntity(null, SAME_IDEMPOTENCY_KEY, 200, now()))))
                .expectError(DuplicateKeyException.class)
                .verify();
    }

}