package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.domain.DroneDTO;
import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import com.musalasoft.dronefleet.domain.UpdateDroneRequestDTO;
import com.musalasoft.dronefleet.service.DroneService;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private static final int MAX_QUERY_SIZE = 50;

    private final DroneService droneService;

    /**
     * Find all drones
     */
    @GetMapping(consumes = ALL_VALUE)
    Flux<DroneDTO> findAllDrones(@RequestParam(name= "limit", defaultValue = "20") int limit) {
        if (limit < 1) {
            limit = 20;
        }
        return droneService.findAll(Math.min(limit, MAX_QUERY_SIZE));
    }


    /**
     * Register drone
     */
    @PostMapping
    Mono<ResponseEntity<String>> registerDrone(@RequestBody @Valid RegisterDroneRequestDTO request,
                                               @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey) {
        return droneService.registerDrone(request)
                .map(doc -> ResponseEntity.ok(doc.getId()));
    }

    /**
     * Find drone by id.
     */
    @GetMapping(path = "{droneId}", consumes = ALL_VALUE)
    Mono<ResponseEntity<DroneDTO>> findDroneById(@PathVariable("droneId") String droneId) {
        return droneService.findDroneById(droneId)
                .map(ResponseEntity::ok);
    }

    /**
     * Find drone by serial number.
     */
    @GetMapping(path = "by-sn/{serialNumber}", consumes = ALL_VALUE)
    Mono<ResponseEntity<DroneDTO>> findDroneBySerialNumber(@PathVariable("serialNumber") String serialNumber) {
        return droneService.findDroneBySerialNumber(serialNumber)
                .map(ResponseEntity::ok);
    }

    /**
     * Update drone state.
     */
    @PutMapping(path = "{droneId}")
    Mono<ResponseEntity<String>> updateDrone(@PathVariable("droneId") String droneId,
                                             @RequestBody @Validated UpdateDroneRequestDTO request,
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
