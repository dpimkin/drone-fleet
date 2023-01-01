package com.musalasoft.dronefleet.api;

import com.musalasoft.dronefleet.domain.RegisterMedicationRequestDTO;
import com.musalasoft.dronefleet.domain.MedicationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.musalasoft.dronefleet.api.Endpoints.MEDICATION_CRUD_ENDPOINT;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(
        path = MEDICATION_CRUD_ENDPOINT,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MedicationCrudController {

    /**
     * Find all medications.
     */
    @GetMapping(consumes = ALL_VALUE)
    Flux<MedicationDTO> findAllDrones() {
        var dto = new MedicationDTO(); // TODO implement
        return Flux.fromIterable(List.of(dto));
    }

    /**
     * Register medication.
     */
    @PostMapping
    Mono<ResponseEntity<String>> registerMedication(@RequestBody RegisterMedicationRequestDTO request) {
        // TODO implement
        return Mono.just(ResponseEntity.ok().build());
    }

    /**
     * Find medication by id.
     */
    @GetMapping(path = "{medicationId}", consumes = ALL_VALUE)
    Mono<ResponseEntity<MedicationDTO>> findMedicationById(@PathVariable("medicationId") String medicationId) {
        // TODO implement
        var dto = new MedicationDTO();
        return Mono.just(ResponseEntity.ok().body(dto));
    }

    /**
     * Find medication by code.
     */
    @GetMapping(path = "by-code/{code}", consumes = ALL_VALUE)
    Mono<ResponseEntity<MedicationDTO>> findMedicationBySerialNumber(@PathVariable("code") String code) {
        // TODO implement
        var dto = new MedicationDTO();
        return Mono.just(ResponseEntity.ok().body(dto));
    }
}
