package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.DockerizedTestSupport;
import com.musalasoft.dronefleet.domain.DronePayloadDTO;
import com.musalasoft.dronefleet.domain.MedicationPayload;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.util.List;
import java.util.UUID;

import static com.musalasoft.dronefleet.api.Endpoints.DISPATCH_ENDPOINT;
import static com.musalasoft.dronefleet.api.Params.IDEMPOTENCY_KEY_HEADER;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class DispatchControllerTest extends DockerizedTestSupport {

    @Autowired
    private WebTestClient webClient;

    @ParameterizedTest
    @ValueSource(strings = {"fOO", "Bar", "foobar-1", "baz_3"})
    void loadDrone_isOkWithName(String name) {
        loadDrone("30", new DronePayloadDTO().setPayloadList(List.of(generateValidMedicationPayload()
                .setName(name))))
                .expectStatus()
                .isOk();
    }

    @Test
    void loadDrone_withEmptyIdempotencyKey() {
        loadDrone("30", new DronePayloadDTO().setPayloadList(List.of(generateValidMedicationPayload())), "")
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


    private ResponseSpec loadDrone(String droneId, DronePayloadDTO request) {
        return loadDrone(droneId, request, UUID.randomUUID().toString());
    }

    private ResponseSpec loadDrone(String droneSn, DronePayloadDTO request, String idempotencyKey) {
        return webClient.put()
                .uri(DISPATCH_ENDPOINT + '/' + droneSn)
                .header(IDEMPOTENCY_KEY_HEADER, idempotencyKey)
                .bodyValue(request)
                .exchange();
    }
}