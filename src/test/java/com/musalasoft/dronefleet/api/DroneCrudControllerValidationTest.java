package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.domain.DroneModelType;
import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.musalasoft.dronefleet.api.Params.IDEMPOTENCY_KEY_HEADER;
import static com.musalasoft.dronefleet.domain.DroneState.IDLE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class DroneCrudControllerValidationTest {

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
        return new RegisterDroneRequestDTO()
                .setSerialNumber("foobar")
                .setModelType(DroneModelType.MIDDLEWEIGHT)
                .setWeightLimit(450)
                .setBatteryCapacity(80)
                .setState(IDLE);
    }
}