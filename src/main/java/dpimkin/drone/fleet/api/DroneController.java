package dpimkin.drone.fleet.api;

import dpimkin.drone.fleet.boundary.DroneMapper;
import dpimkin.drone.fleet.domain.DroneDTO;
import dpimkin.drone.fleet.domain.RegisterDroneRequestDTO;
import dpimkin.drone.fleet.domain.UpdateDroneRequestDTO;
import dpimkin.drone.fleet.service.DroneService;
import dpimkin.drone.fleet.service.OperationLogService;
import dpimkin.drone.fleet.service.StalledOperationException;
import dpimkin.drone.fleet.service.UpdateDroneDTO;
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

import static dpimkin.drone.fleet.api.Endpoints.DRONE_CRUD_ENDPOINT;
import static dpimkin.drone.fleet.api.Params.IDEMPOTENCY_KEY_HEADER;
import static dpimkin.drone.fleet.boundary.IdempotencyUtils.isInvalidIdempotencyKey;
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

        return droneService.findDrones(Math.min(limit, MAX_QUERY_SIZE))
                .map(mapper::mapDroneEntity);
    }

    /**
     * Register drone
     */
    @PostMapping
    Mono<ResponseEntity<String>> registerDrone(@RequestBody @Valid RegisterDroneRequestDTO request) {
        return droneService.registerDrone(request)
                .onErrorResume(DuplicateKeyException.class,
                        e -> droneService.findDroneBySn(request.getSerialNumber()))
                .map(doc -> ResponseEntity.ok(Long.toString(doc.id())));
    }

    /**
     * Find drone by serial number.
     */
    @GetMapping(path = "{serialNumber}", consumes = ALL_VALUE)
    Mono<ResponseEntity<DroneDTO>> findDroneBySerialNumber(@PathVariable("serialNumber") String serialNumber) {
        return droneService.findDroneBySn(serialNumber)
                .map(mapper::mapDroneEntity)
                .map(ResponseEntity::ok);
    }

      /**
     * Update drone by serial number.
     */
    @PutMapping(path = "{serialNumber}")
    Mono<ResponseEntity<String>> updateDroneBySn(@PathVariable("serialNumber") String serialNumber,
                                                 @RequestBody @Validated UpdateDroneRequestDTO requestBody,
                                                 @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey) {

        if (isInvalidIdempotencyKey(idempotencyKey)) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        var request = new UpdateDroneDTO()
                .setSerialNumber(serialNumber)
                .setBatteryCapacity(requestBody.getBatteryCapacity())
                .setState(requestBody.getState())
                .setIdempotencyKey(serialNumber + idempotencyKey); // reduces a number of collision

        return droneService.updateDroneBySerialNumber(request)

                // get stalled operation result
                .onErrorResume(StalledOperationException.class,
                        (e) -> operationLogService.fetchOperationResult(request.getIdempotencyKey()))

                .map(status -> ResponseEntity.status(status).body(""));
    }
}
