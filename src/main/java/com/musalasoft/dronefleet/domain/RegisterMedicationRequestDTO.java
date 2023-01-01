package com.musalasoft.dronefleet.domain;

import lombok.Data;

@Data
public class RegisterMedicationRequestDTO {
    String name;
    String code;
    int weight;
    String imageUrl;

}
