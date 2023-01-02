package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.boundary.DroneMapper;
import com.musalasoft.dronefleet.domain.DroneDTO;
import com.musalasoft.dronefleet.domain.RegisterDroneRequestDTO;
import com.musalasoft.dronefleet.domain.UpdateDroneRequestDTO;
import com.musalasoft.dronefleet.service.DroneService;
import com.musalasoft.dronefleet.service.LowBatteryException;
import com.musalasoft.dronefleet.service.OperationLogService;
import com.musalasoft.dronefleet.service.StalledOperationException;
import com.musalasoft.dronefleet.service.UpdateDroneRequestByIdDTO;
import com.musalasoft.dronefleet.service.UpdateDroneRequestBySerialNumberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import static com.musalasoft.dronefleet.api.Endpoints.DRONE_CRUD_ENDPOINT;
import static com.musalasoft.dronefleet.api.Params.IDEMPOTENCY_KEY_HEADER;
import static com.musalasoft.dronefleet.boundary.IdempotencyUtils.isInvalidIdempotencyKey;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(
        path = DRONE_CRUD_ENDPOINT,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DroneController {
    private static final int MAX_QUERY_SIZE = 50;

    private final DroneService droneService;
    private final OperationLogService operationLogService;

    private final DroneMapper mapper;

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
    Mono<ResponseEntity<String>> registerDrone(@RequestBody @Valid RegisterDroneRequestDTO request) {
        return droneService.registerDrone(request)
                .onErrorResume(DuplicateKeyException.class,
                        e -> droneService.findDroneBySerialNumber(request.getSerialNumber()))
                .map(doc -> ResponseEntity.ok(Long.toString(doc.id())));
    }

    /**
     * Find drone by id.
     */
    @GetMapping(path = "{droneId}", consumes = ALL_VALUE)
    Mono<ResponseEntity<DroneDTO>> findDroneById(@PathVariable("droneId") Long droneId) {
        return droneService.findDroneById(droneId)
                .map(mapper::mapDroneEntity)
                .map(ResponseEntity::ok);
    }

    /**
     * Find drone by serial number.
     */
    @GetMapping(path = "by-sn/{serialNumber}", consumes = ALL_VALUE)
    Mono<ResponseEntity<DroneDTO>> findDroneBySerialNumber(@PathVariable("serialNumber") String serialNumber) {
        return droneService.findDroneBySerialNumber(serialNumber)
                .map(mapper::mapDroneEntity)
                .map(ResponseEntity::ok);
    }

    /**
     * Update drone by id.
     */
    @PutMapping(path = "{droneId}")
    Mono<ResponseEntity<String>> updateDroneById(@PathVariable("droneId") Long droneId,
                                                 @RequestBody @Validated UpdateDroneRequestDTO request,
                                                 @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey) {
        return Mono.empty();
//        if (isInvalidIdempotencyKey(idempotencyKey)) {
//            return Mono.just(ResponseEntity.badRequest().build());
//        }
//
//        final var updateDroneRequestById = new UpdateDroneRequestByIdDTO().setId(droneId);
//        updateDroneRequestById.setState(request.getState());
//        updateDroneRequestById.setBatteryCapacity(request.getBatteryCapacity());
//        updateDroneRequestById.setIdempotencyKey(droneId + idempotencyKey);
//
//
//        return droneService.updateDroneById(updateDroneRequestById)
//
//                .onErrorResume(StalledOperationException.class, (e) -> operationLogService.fetchOperationResult(updateDroneRequestById.getIdempotencyKey()))
////                .setId(droneId)
////                        .setState(request.getState())
////                                .setBatteryCapacity(request.get)
////
////        droneService.updateDroneById()
//        // TODO implement
//        //return Mono.just(ResponseEntity.ok().build());
    }

    /**
     * Update drone by serial number.
     */
    @PutMapping(path = "sn/{serialNumber}")
    Mono<ResponseEntity<String>> updateDroneBySn(@PathVariable("serialNumber") String serialNumber,
                                                 @RequestBody @Validated UpdateDroneRequestDTO requestBody,
                                                 @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey) {

        if (isInvalidIdempotencyKey(idempotencyKey)) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        var request = new UpdateDroneRequestBySerialNumberDTO()
                .setSerialNumber(serialNumber)
                .setBatteryCapacity(requestBody.getBatteryCapacity())
                .setState(requestBody.getState())
                .setIdempotencyKey(serialNumber + idempotencyKey);

        var result = droneService.updateDroneBySerialNumber(request)

                // get stalled operation result
                .onErrorResume(StalledOperationException.class,
                        (e) -> operationLogService.fetchOperationResult(request.getIdempotencyKey()))

                .map(status -> {
                    log.info("149 {}", status);
                    return ResponseEntity.status(status).body("");
                });


        return result;

//        // TODO implement
//        return Mono.just(ResponseEntity.ok().build());
    }

}
