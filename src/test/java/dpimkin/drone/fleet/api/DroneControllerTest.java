package dpimkin.drone.fleet.api;

import dpimkin.drone.fleet.DockerizedTestSupport;
import dpimkin.drone.fleet.domain.DroneDTO;
import dpimkin.drone.fleet.domain.DroneModelType;
import dpimkin.drone.fleet.domain.RegisterDroneRequestDTO;
import dpimkin.drone.fleet.domain.UpdateDroneRequestDTO;
import dpimkin.drone.fleet.domain.DroneState;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "logging.level.io.r2dbc.postgresql.QUERY=DEBUG",
        "logging.level.io.r2dbc.postgresql.PARAM=DEBUG"})
class DroneControllerTest {
    @Container
    final static PostgreSQLContainer<?> POSTGRES = DockerizedTestSupport.createDatabaseInstance();

    private static int droneSerialNumberCounter = 1;

    @Autowired
    private WebTestClient webClient;

    @Test
    void registerDrone_isOk() {
        registerDrone(generateValidDroneRequestDTO())
                .expectStatus()
                .isOk();
    }


    /**
     * ensure that everything would be fine with poor network connection.
     */
    @Test
    void updateDrone_withStalledSuccessfulOperation() {
        final var idempotencyKey = "1qazxsw2";
        final var expectedSerialNumber = "STALLEDOK";
        final var expectedState = DroneState.DELIVERED;
        final var expectedType = DroneModelType.HEAVYWEIGHT;
        final var expectedBatteryCapacity = 11;
        final var expectedWeightLimit = 250;

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

        updateDrone(expectedSerialNumber, new UpdateDroneRequestDTO().setState(DroneState.IDLE)
                .setBatteryCapacity(25), idempotencyKey)
                .expectStatus()
                .isOk();

        updateDrone(expectedSerialNumber, new UpdateDroneRequestDTO().setState(DroneState.IDLE)
                .setBatteryCapacity(25), idempotencyKey)
                .expectStatus()
                .isOk();

        // TODO
//        var actualResult = fetchDroneById(droneId)
//                .expectStatus()
//                .isOk()
//                .expectBody(DroneDTO.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertEquals(droneId, actualResult.getId().longValue());
//        assertEquals(expectedBatteryCapacity, actualResult.getBatteryCapacity().intValue());
//        assertEquals(expectedWeightLimit, actualResult.getWeightLimit().intValue());
//        assertEquals(expectedState, actualResult.getState());
//        assertEquals(expectedType, actualResult.getModelType());




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

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1", "0"})
    void registerDrone_withBadWeightLimig(Integer weightLimit) {
        registerDrone(generateValidDroneRequestDTO().setWeightLimit(weightLimit))
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void fetchDrone_isOk() {
        final var droneId = 8;
        final var expectedSerialNumber = "007";
        final var expectedState = DroneState.DELIVERING;
        final var expectedType = DroneModelType.HEAVYWEIGHT;
        final var expectedBatteryCapacity = 24;
        final var expectedWeightCap = 100;
        final var expectedWeightLimit = 450;

        var actualResult = fetchDrone(expectedSerialNumber)
                .expectStatus()
                .isOk()
                .expectBody(DroneDTO.class)
                .returnResult()
                .getResponseBody();

        assertEquals(droneId, actualResult.getId().longValue());
        assertEquals(expectedBatteryCapacity, actualResult.getBatteryCapacity().intValue());
        assertEquals(expectedWeightLimit, actualResult.getWeightLimit().intValue());
        assertEquals(expectedWeightCap, actualResult.getWeightCapacity().intValue());
        assertEquals(expectedState, actualResult.getState());
        assertEquals(expectedType, actualResult.getModelType());
    }


    private ResponseSpec updateDrone(String sn, UpdateDroneRequestDTO body, String idempotencyKey) {
        return webClient.put().uri(Endpoints.DRONE_CRUD_ENDPOINT + '/' + sn)
                .header(Params.IDEMPOTENCY_KEY_HEADER, idempotencyKey)
                .bodyValue(body)
                .exchange();

    }

    private ResponseSpec registerDrone(RegisterDroneRequestDTO request) {
        return webClient.post()
                .uri(Endpoints.DRONE_CRUD_ENDPOINT)
                .bodyValue(request)
                .exchange();
    }


    private ResponseSpec fetchDrone(String sn) {
        return webClient.get()
                .uri(Endpoints.DRONE_CRUD_ENDPOINT + '/' + sn)
                .exchange();
    }


    private RegisterDroneRequestDTO generateValidDroneRequestDTO() {
        var sn = ++droneSerialNumberCounter;
        return new RegisterDroneRequestDTO()
                .setSerialNumber("foobar" + sn)
                .setModelType(DroneModelType.MIDDLEWEIGHT)
                .setWeightLimit(450)
                .setBatteryCapacity(80)
                .setState(DroneState.IDLE);
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // region Flyway
        registry.add("spring.flyway.url", POSTGRES::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
        registry.add("spring.flyway.driver-class-name", POSTGRES::getDriverClassName);
        // endregion


        // region R2DBC
        registry.add("spring.r2dbc.url", () -> POSTGRES.getJdbcUrl().replace("jdbc", "r2dbc:pool"));
        registry.add("spring.r2dbc.username", POSTGRES::getUsername);
        registry.add("spring.r2dbc.password", POSTGRES::getPassword);
        // endregion
    }
}