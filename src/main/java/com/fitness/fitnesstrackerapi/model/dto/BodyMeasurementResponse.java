package com.fitness.fitnesstrackerapi.model.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyMeasurementResponse {

    private Long id;
    private LocalDate date;
    private String part;
    private Double value;
}
