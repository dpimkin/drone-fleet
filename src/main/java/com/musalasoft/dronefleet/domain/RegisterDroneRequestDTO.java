package com.musalasoft.dronefleet.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterDroneRequestDTO {

    @NotNull(message = "serialNumber should be specified")
    @Pattern(regexp = "^[A-Za-z0-9]{1,100}$")
    String serialNumber;

    @NotNull(message = "modelType should be specified")
    DroneModelType modelType;

    @NotNull(message = "batteryCapacity should be specified")
    @Min(value = 0, message = "Required batteryCapacity is range [0..100]")
    @Max(value = 100, message = "Required batteryCapacity is range [0..100]")
    Integer batteryCapacity;

    @NotNull(message = "weightLimit should be specified")
    @Min(value = 1, message = "Required weightLimit is range [1..500]")
    @Max(value = 500, message = "Required weightLimit is range [1..500]")
    Integer weightLimit;

    @NotNull(message = "state should be specified")
    DroneState state;
}
