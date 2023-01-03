package dpimkin.drone.fleet.api;

import dpimkin.drone.fleet.boundary.DroneMapper;
import dpimkin.drone.fleet.domain.DroneDTO;
import dpimkin.drone.fleet.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static dpimkin.drone.fleet.api.Endpoints.AUDIT_ENDPOINT;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = AUDIT_ENDPOINT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    private final DroneMapper droneMapper;

    @GetMapping(consumes = ALL_VALUE)
    Flux<DroneDTO> find() {
        return auditService.findDronesWithLowBattery().map(droneMapper::mapDroneEntity);
    }

    @Scheduled(fixedDelayString = "${drone-fleet.audit.rateMs:60000}")
    void logDronesWithLowBattery() {
        auditService.findDronesWithLowBattery().toStream(10)
                .forEach(drone -> log.warn("{} drone with s/n {} has low battery {}%",
                        drone.state(),
                        drone.serialNumber(),
                        drone.batteryCapacity()));
    }


}
