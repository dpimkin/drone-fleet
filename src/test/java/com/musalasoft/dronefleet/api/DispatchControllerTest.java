package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.DockerizedTestSupport;
import com.musalasoft.dronefleet.domain.DronePayloadDTO;
import com.musalasoft.dronefleet.domain.MedicationPayload;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.musalasoft.dronefleet.api.Endpoints.DISPATCH_ENDPOINT;
import static com.musalasoft.dronefleet.api.Params.IDEMPOTENCY_KEY_HEADER;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {
        "logging.level.io.r2dbc.postgresql.QUERY=DEBUG",
        "logging.level.io.r2dbc.postgresql.PARAM=DEBUG"})
class DispatchControllerTest {

    private static final String POSTGRESQL_IMAGE = "postgres:13.7-alpine";

    @Container
    final static PostgreSQLContainer<?> POSTGRES = createDatabaseInstance();

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // region Flyway
        registry.add("spring.flyway.url", DispatchControllerTest::getPostgresqlUrlForFlyway);
        registry.add("spring.flyway.user", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
        registry.add("spring.flyway.driver-class-name", POSTGRES::getDriverClassName);
        // endregion


        // region R2DBC
        registry.add("spring.r2dbc.url", DispatchControllerTest::getPostgresqlUrlForR2dbc);
        registry.add("spring.r2dbc.username", POSTGRES::getUsername);
        registry.add("spring.r2dbc.password", POSTGRES::getPassword);
        // endregion
    }

    private static PostgreSQLContainer<?> createDatabaseInstance() {
        int containerPort = 5432 ;
        int localPort = 5432 ;
        PostgreSQLContainer<?> container = new PostgreSQLContainer(POSTGRESQL_IMAGE) {{
            if (TRUE.toString().equalsIgnoreCase(System.getProperty("DEBUG"))) {
                this.withDatabaseName("db").withUsername("user").withPassword("1q2w3e");
                this.addFixedExposedPort(localPort, containerPort);
            }
        }};
        return container;
    }


    private static String getPostgresqlUrlForFlyway() {
        var result = POSTGRES.getJdbcUrl();
        log.info("spring.flyway.url={}", result);
        return result;
    }

    private static String getPostgresqlUrlForR2dbc() {
        var result = POSTGRES.getJdbcUrl().replace("jdbc", "r2dbc:pool");
        log.info("spring.r2dbc.url={}", result);
        return result;
    }

    @Autowired
    private WebTestClient webClient;

    @ParameterizedTest
    @ValueSource(strings = {"fOO", "Bar", "foobar-1", "baz_3"})
    void loadDrone_isOkWithName(String name) {
        var droneSn = "006";

        loadDrone(droneSn, new DronePayloadDTO().setPayloadList(List.of(generateValidMedicationPayload()
                .setName(name))))
                .expectStatus()
                .isOk();

        assertTrue(getDronePayload(droneSn)
                .expectStatus()
                .isOk()
                .expectBodyList(MedicationPayload.class)
                .returnResult()
                .getResponseBody()
                .stream()
                .anyMatch(payload -> payload.getName().equals(name)));
    }

    @Test
    void loadDrone_withEmptyIdempotencyKey() {
        loadDrone("30", new DronePayloadDTO().setPayloadList(List.of(generateValidMedicationPayload())), "")
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void loadDrone_withEmptyMedications() {
        loadDrone("30", new DronePayloadDTO().setPayloadList(new ArrayList<>()))
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"#", "*", "-", "тест", ""})
    void loadDrone_withBadCode(String code) {
        loadDrone("39", new DronePayloadDTO().setPayloadList(List.of(generateValidMedicationPayload()
                .setCode(code))))
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"#", "*", "тест", ""})
    void loadDrone_withBadName(String name) {
        loadDrone("39", new DronePayloadDTO().setPayloadList(List.of(generateValidMedicationPayload()
                .setName(name))))
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1", "0"})
    void loadDrone_withBadQty(Integer qty) {
        loadDrone("49", new DronePayloadDTO().setPayloadList(List.of(generateValidMedicationPayload()
                .setQty(qty))))
                .expectStatus()
                .isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1", "0"})
    void loadDrone_withBadWeight(Integer weight) {
        loadDrone("49", new DronePayloadDTO().setPayloadList(List.of(generateValidMedicationPayload()
                .setWeight(weight))))
                .expectStatus()
                .isBadRequest();
    }

    private MedicationPayload generateValidMedicationPayload() {
        return new MedicationPayload()
                .setName("NZT-48")
                .setCode("NZT_48")
                .setWeight(1)
                .setQty(2);
    }

    private ResponseSpec getDronePayload(String droneSn) {
        return webClient.get()
                .uri(DISPATCH_ENDPOINT + '/' + droneSn)
                .exchange();

    }

    private ResponseSpec loadDrone(String droneSn, DronePayloadDTO request) {
        return loadDrone(droneSn, request, UUID.randomUUID().toString());
    }

    private ResponseSpec loadDrone(String droneSn, DronePayloadDTO request, String idempotencyKey) {
        return webClient.put()
                .uri(DISPATCH_ENDPOINT + '/' + droneSn)
                .header(IDEMPOTENCY_KEY_HEADER, idempotencyKey)
                .bodyValue(request)
                .exchange();
    }
}