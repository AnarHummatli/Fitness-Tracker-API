package com.fitness.fitnesstrackerapi.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExerciseRequest {

    @NotBlank
    private String name;

    private String description;
}
