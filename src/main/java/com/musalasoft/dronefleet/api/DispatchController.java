package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.boundary.DroneMapper;
import com.musalasoft.dronefleet.domain.DroneDTO;
import com.musalasoft.dronefleet.domain.DronePayloadDTO;
import com.musalasoft.dronefleet.service.DispatchService;
import com.musalasoft.dronefleet.service.DroneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.musalasoft.dronefleet.api.Endpoints.DISPATCH_ENDPOINT;
import static com.musalasoft.dronefleet.api.Params.IDEMPOTENCY_KEY_HEADER;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RestController
@RequestMapping(
        path = DISPATCH_ENDPOINT,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DispatchController {
    private final DispatchService dispatchService;
    private final DroneService droneService;
    private final DroneMapper droneMapper;


    @PutMapping(path = "load-by-id/{droneId}")
    Mono<ResponseEntity<String>> loadDroneById(@Valid @RequestBody DronePayloadDTO request,
                                               @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey,
                                               @PathVariable("droneId") String droneId) {
        // TODO implement
        return Mono.just(ResponseEntity.ok().build());
    }

    @PutMapping(path = "load-by-sn/{droneSn}")
    Mono<ResponseEntity<String>> loadDroneBySerialNumber(@Valid @RequestBody DronePayloadDTO request,
                                                         @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey,
                                                         @PathVariable("droneSn") String droneSn) {
        // TODO implement
        return Mono.just(ResponseEntity.ok().build());
    }

    @GetMapping(path = "available-drones", consumes = ALL_VALUE)
    Flux<DroneDTO> findAvailableDrones(@RequestParam(name= "limit", defaultValue = "20") int limit) {
        if (limit < 1) {
            limit = 20;
        }

        return dispatchService.findAvailableDrones(limit)
                .map(droneMapper::mapDroneEntity);
    }

    @GetMapping("payload-by-id/{droneId}")
    Mono<DronePayloadDTO> findDronePayloadByDroneId(@PathVariable("droneId") String droneId) {
        // TODO implement
        return Mono.empty();
    }

    @GetMapping("payload-by-sn/{droneSn}")
    Mono<DronePayloadDTO> findDronePayloadByDroneSerialNumber(@PathVariable("droneSn") String droneSn) {
        // TODO implement
        return Mono.empty();
    }

}
