package com.fitness.fitnesstrackerapi.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyMeasurementRequest {

    @NotNull
    private LocalDate date;

    @NotBlank
    private String part;

    @NotNull
    @Positive
    private Double value;
}
