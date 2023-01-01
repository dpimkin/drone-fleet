package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import com.musalasoft.dronefleet.domain.DroneDTO;
import com.musalasoft.dronefleet.domain.UpdateDroneRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.musalasoft.dronefleet.api.Endpoints.DRONE_CRUD_ENDPOINT;
import static com.musalasoft.dronefleet.api.Params.IDEMPOTENCY_KEY_HEADER;
import static com.musalasoft.dronefleet.domain.DroneModelType.LIGHTWEIGHT;
import static com.musalasoft.dronefleet.domain.DroneState.IDLE;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(
        path = DRONE_CRUD_ENDPOINT,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DroneCrudController {

    /**
     * Find all drones
     */
    @GetMapping(consumes = ALL_VALUE)
    Flux<DroneDTO> findAllDrones() {
        // TODO implement
        return Flux.fromIterable(List.of(new DroneDTO().setId("foobar")
                .setSerialNumber("123")
                .setModelType(LIGHTWEIGHT)
                .setWeightCapacity(300)
                .setWeightLimit(500)
                .setState(IDLE)));
    }


    /**
     * Register drone
     */
    @PostMapping
    Mono<ResponseEntity<String>> registerDrone(@RequestBody RegisterDroneRequestDTO request,
                                               @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey) {
        // TODO implement
        return Mono.just(ResponseEntity.ok().build());
    }

    /**
     * Find drone by id.
     */
    @GetMapping(path = "{droneId}", consumes = ALL_VALUE)
    Mono<ResponseEntity<DroneDTO>> findDroneById(@PathVariable("droneId") String droneId) {
        // TODO implement
        return Mono.just(ResponseEntity.ok().body(new DroneDTO().setId("foo")
                .setSerialNumber("11101")
                .setModelType(LIGHTWEIGHT)
                .setWeightCapacity(10)
                .setWeightLimit(500)
                .setState(IDLE)));
    }

    /**
     * Find drone by serial number.
     */
    @GetMapping(path = "by-sn/{serialNumber}", consumes = ALL_VALUE)
    Mono<ResponseEntity<DroneDTO>> findDroneBySerialNumber(@PathVariable("serialNumber") String serialNumber) {
        // TODO implement
        return Mono.just(ResponseEntity.ok().body(new DroneDTO().setId("bar")
                .setSerialNumber("123")
                .setModelType(LIGHTWEIGHT)
                .setWeightCapacity(10)
                .setWeightLimit(500)
                .setState(IDLE)));
    }

    /**
     * Update drone state.
     */
    @PutMapping(path = "{droneId}")
    Mono<ResponseEntity<String>> updateDrone(@PathVariable("droneId") String droneId,
                                             @RequestBody UpdateDroneRequestDTO request,
                                             @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey) {
        // TODO implement
        return Mono.just(ResponseEntity.ok().build());
    }

    /**
     * Mark drone as deleted
     */
    @DeleteMapping(path = "{droneId}", consumes = ALL_VALUE)
    Mono<ResponseEntity<String>> deleteDrone(@PathVariable("droneId") String droneId) {
        // TODO implement
        return Mono.just(ResponseEntity.ok().build());
    }
}
