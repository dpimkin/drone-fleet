package com.musalasoft.dronefleet.persistence;

import com.musalasoft.dronefleet.DockerizedSupport;
import com.musalasoft.dronefleet.domain.DroneModelType;
import com.musalasoft.dronefleet.domain.DroneState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static com.musalasoft.dronefleet.domain.DroneModelType.HEAVYWEIGHT;
import static com.musalasoft.dronefleet.domain.DroneModelType.LIGHTWEIGHT;
import static com.musalasoft.dronefleet.domain.DroneState.IDLE;
import static com.musalasoft.dronefleet.domain.DroneState.LOADING;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

//import com.musalasoft.dronefleet.DockerizedSupport;
//import com.musalasoft.dronefleet.service.DroneService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.test.context.TestPropertySource;
//import reactor.test.StepVerifier;
//
//import static java.time.Instant.now;
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {
        "logging.level.io.r2dbc.postgresql.QUERY=DEBUG",
        "logging.level.io.r2dbc.postgresql.PARAM=DEBUG"})
class PreconditionsTest extends DockerizedSupport {
    private static final String SAME_SERIAL_NUMBER ="1101";
    private static final String SAME_IDEMPOTENCY_KEY = "foobar";

    @Autowired
    ReactiveDroneRepository droneRepository;

    @Autowired
    IdempotentOperationRepository idempotentOperationRepository;

    @Test
    void droneSerialNumber_shouldBeUnique() {
       droneRepository.save(new DroneEntity(null, SAME_SERIAL_NUMBER, LIGHTWEIGHT, IDLE)).block();
        droneRepository.save(new DroneEntity(null, SAME_SERIAL_NUMBER, HEAVYWEIGHT, LOADING)).block();



    }



//    @Autowired
//    DroneService droneService;
//
//    @Test
//    void droneSerialNumber_shouldBeUnique() {
//        StepVerifier.create(droneRepository.save(new DroneDocument()
//                                .setSerialNumber(SAME_SERIAL_NUMBER))
//                        .flatMap(doc -> droneRepository.save(new DroneDocument()
//                                .setSerialNumber(SAME_SERIAL_NUMBER))))
//                .expectError(DuplicateKeyException.class)
//                .verify();
//    }
//
//    @Test
//    void idempotencyKey_shouldBeUnique() {
//        StepVerifier.create(idempotentOperationRepository.save(new IdempotentOperationDocument()
//                                .setIdempotencyKey(SAME_IDEMPOTENCY_KEY)
//                                .setCreated(now()))
//                        .flatMap(doc -> idempotentOperationRepository.save((new IdempotentOperationDocument()
//                                .setIdempotencyKey(SAME_IDEMPOTENCY_KEY)))))
//                .expectError(DuplicateKeyException.class)
//                .verify();
//    }
//
//    @Test
//    void todo() {
//        try {
//            var l = droneService.test("QWERT").block();
//
//            System.out.println(l.getId());
//
//        } finally {
//            var r = droneRepository.findBySerialNumberAndDeleted("QWERT", false).block();
//            System.out.println("found " + r.getId());
//        }
//
//
//
//
//    }
}