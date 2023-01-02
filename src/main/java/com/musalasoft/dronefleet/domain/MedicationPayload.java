package com.musalasoft.dronefleet.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MedicationPayload {

    @NotBlank(message = "name is required")
    @Pattern(regexp = "([A-Za-z0-9_-]+)", message = "allowed only letters, numbers, ‘-‘, ‘_’")
    String name;

    @NotBlank(message = "code is required")
    @Pattern(regexp = "([A-Z0-9_]+)", message = "allowed only upper case letters, underscore and numbers")
    String code;

    @NotNull(message = "weight is required")
    @Positive
    Integer weight;

    String image;

    @NotNull(message = "qty is required")
    @Positive
    Integer qty;
}
