package dpimkin.drone.fleet.api;

import dpimkin.drone.fleet.boundary.DroneMapper;
import dpimkin.drone.fleet.boundary.MedicationMapper;
import dpimkin.drone.fleet.domain.DroneDTO;
import dpimkin.drone.fleet.domain.DronePayloadDTO;
import dpimkin.drone.fleet.domain.MedicationPayload;
import dpimkin.drone.fleet.domain.WeightUtil;
import dpimkin.drone.fleet.service.DispatchService;
import dpimkin.drone.fleet.service.LoadDroneDTO;
import dpimkin.drone.fleet.service.OperationLogService;
import dpimkin.drone.fleet.service.StalledOperationException;
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

import static dpimkin.drone.fleet.api.Endpoints.DISPATCH_ENDPOINT;
import static dpimkin.drone.fleet.api.Params.IDEMPOTENCY_KEY_HEADER;
import static dpimkin.drone.fleet.boundary.IdempotencyUtils.isInvalidIdempotencyKey;
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

    private final OperationLogService operationLogService;
    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;


    @PutMapping(path = "{droneSn}")
    Mono<ResponseEntity<String>> loadDrone(@Valid @RequestBody DronePayloadDTO requestBody,
                                           @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey,
                                           @PathVariable("droneSn") String droneSn) {

        if (isInvalidIdempotencyKey(idempotencyKey)) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        var request = new LoadDroneDTO()
                .setIdempotencyKey(droneSn + idempotencyKey)
                .setSerialNumber(droneSn)
                .setRequiredWeight(WeightUtil.calculateWeight(requestBody.getPayloadList()))
                .setPayloadList(requestBody.getPayloadList());

        return dispatchService.loadDrone(request)
                // get stalled operation result
                .onErrorResume(StalledOperationException.class,
                        (e) -> operationLogService.fetchOperationResult(request.getIdempotencyKey()))

                .map(status -> ResponseEntity.status(status).body(""));
    }

    @GetMapping(path = "available-drones", consumes = ALL_VALUE)
    Flux<DroneDTO> findAvailableDrones(@RequestParam(name= "limit", defaultValue = "20") int limit) {
        if (limit < 1) {
            limit = 20;
        }

        return dispatchService.findAvailableDrones(limit)
                .map(droneMapper::mapDroneEntity);
    }

    @GetMapping(path = "{droneSerialNumber}", consumes = ALL_VALUE)
    Flux<MedicationPayload> findDronePayloadByDroneSerialNumber(@PathVariable("droneSerialNumber") String droneSn) {
        return dispatchService.findMedicationPayloadByDroneSn(droneSn)
                .map(medicationMapper::mapMedicationPayloadEntity);
    }


}
