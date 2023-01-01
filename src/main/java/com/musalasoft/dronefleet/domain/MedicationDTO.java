package com.musalasoft.dronefleet.domain;

import lombok.Data;

@Data
public class MedicationDTO {
    String id;
    String name;
    String code;
    int weight;
    String imageUrl;
}
