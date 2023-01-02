package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.DockerizedMongoSupport;
import com.musalasoft.dronefleet.domain.DroneModelType;
import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.musalasoft.dronefleet.api.Params.IDEMPOTENCY_KEY_HEADER;
import static com.musalasoft.dronefleet.domain.DroneState.IDLE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = "logging.level.org.springframework.data.mongodb.core.MongoTemplate=TRACE")
class DroneCrudControllerTest extends DockerizedMongoSupport {

    private static int droneSerialNumberCounter = 1;

    @Autowired
    private WebTestClient webClient;

    @Test
    void registerDrone_isOk() {
        webClient.post()
                .uri(Endpoints.DRONE_CRUD_ENDPOINT)
                .header(IDEMPOTENCY_KEY_HEADER, "")
                .bodyValue(generateValidDroneRequestDTO())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Disabled
    void registerDrone_failsWithEmptyIdepmpotencyKey() {
        webClient.post()
                .uri(Endpoints.DRONE_CRUD_ENDPOINT)
                .header(IDEMPOTENCY_KEY_HEADER, "")
                .bodyValue(generateValidDroneRequestDTO())
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1", "101"})
    void registerDrone_withBadBatteryCapacity(Integer batteryCapacity) {
        webClient.post()
                .uri(Endpoints.DRONE_CRUD_ENDPOINT)
                .header(IDEMPOTENCY_KEY_HEADER, "")
                .bodyValue(generateValidDroneRequestDTO().setBatteryCapacity(batteryCapacity))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private RegisterDroneRequestDTO generateValidDroneRequestDTO() {
        var sn = ++droneSerialNumberCounter;
        return new RegisterDroneRequestDTO()
                .setSerialNumber("foobar" + sn)
                .setModelType(DroneModelType.MIDDLEWEIGHT)
                .setWeightLimit(450)
                .setBatteryCapacity(80)
                .setState(IDLE);
    }
}