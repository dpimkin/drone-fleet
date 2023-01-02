package com.musalasoft.dronefleet.persistence;

import com.mongodb.DuplicateKeyException;
import com.musalasoft.dronefleet.DockerizedMongoSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MongoPreconditionsTest extends DockerizedMongoSupport {
    private static final String SAME_SERIAL_NUMBER ="1101";

    @Autowired
    ReactiveDroneRepository dao;

    @Test
    void droneSerialNumber_shouldBeUnique() {
        StepVerifier.create(dao.save(new DroneDocument()
                                .setSerialNumber(SAME_SERIAL_NUMBER))
                        .flatMap(doc -> dao.save(new DroneDocument()
                                .setSerialNumber(SAME_SERIAL_NUMBER))))
                .expectError(DuplicateKeyException.class)
                .verify();
    }


}