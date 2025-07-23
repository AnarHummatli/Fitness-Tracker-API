package com.fitness.fitnesstrackerapi.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WorkoutEntryRequest {

    @NotNull
    private Long exerciseId;

    @NotNull
    @Min(1)
    private Integer sets;

    @NotNull
    @Min(1)
    private Integer reps;

    @NotNull
    @Positive
    private Double weight;
}
