package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.DockerizedTestSupport;
import com.musalasoft.dronefleet.domain.DroneDTO;
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
import static com.musalasoft.dronefleet.domain.DroneModelType.CRUISERWEIGHT;
import static com.musalasoft.dronefleet.domain.DroneState.IDLE;
import static com.musalasoft.dronefleet.domain.DroneState.RETURNING;
import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "logging.level.io.r2dbc.postgresql.QUERY=DEBUG",
        "logging.level.io.r2dbc.postgresql.PARAM=DEBUG"})
class DroneControllerTest extends DockerizedTestSupport {

    private static int droneSerialNumberCounter = 1;

    @Autowired
    private WebTestClient webClient;

    @Test
    void registerDrone_isOk() {
        registerDrone(generateValidDroneRequestDTO())
                .expectStatus()
                .isOk();
    }


    @Test
    void fetchDroneById_isOk() {
        final var expectedSerialNumber = "FETCHDRONEBYID";
        final var expectedState = RETURNING;
        final var expectedType = CRUISERWEIGHT;
        final var expectedBatteryCapacity = 73;
        final var expectedWeightLimit = 330;

        var provisioning = registerDrone(new RegisterDroneRequestDTO()
                .setSerialNumber(expectedSerialNumber)
                .setWeightLimit(expectedWeightLimit)
                .setBatteryCapacity(expectedBatteryCapacity)
                .setModelType(expectedType)
                .setState(expectedState))
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        long droneId = Long.parseLong(provisioning);

        var actualResult = fetchDroneById(droneId)
                .expectStatus()
                .isOk()
                .expectBody(DroneDTO.class)
                .returnResult()
                .getResponseBody();

        assertEquals(droneId, actualResult.getId().longValue());
        assertEquals(expectedBatteryCapacity, actualResult.getBatteryCapacity().intValue());
        assertEquals(expectedWeightLimit, actualResult.getWeightLimit().intValue());
        assertEquals(expectedState, actualResult.getState());
        assertEquals(expectedType, actualResult.getModelType());
    }


    @Test
    void registerDrone_withoutModelType() {
        registerDrone(generateValidDroneRequestDTO().setModelType(null))
                .expectStatus()
                .isBadRequest();
    }
    @Test
    void registerDrone_withoutState() {
        registerDrone(generateValidDroneRequestDTO().setState(null))
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1", "101"})
    void registerDrone_withBadBatteryCapacity(Integer batteryCapacity) {
        registerDrone(generateValidDroneRequestDTO().setBatteryCapacity(batteryCapacity))
                .expectStatus()
                .isBadRequest();
    }


    private WebTestClient.ResponseSpec registerDrone(RegisterDroneRequestDTO request) {
        return webClient.post()
                .uri(Endpoints.DRONE_CRUD_ENDPOINT)
                .bodyValue(request)
                .exchange();
    }


    private WebTestClient.ResponseSpec fetchDroneById(long id) {
        return webClient.get()
                .uri(Endpoints.DRONE_CRUD_ENDPOINT + '/' + id)
                .exchange();
    }

    private WebTestClient.ResponseSpec fetchDroneBySerialNumber(String sn) {
        return webClient.get()
                .uri(Endpoints.DRONE_CRUD_ENDPOINT + "/by-sn/" + sn)
                .exchange();
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