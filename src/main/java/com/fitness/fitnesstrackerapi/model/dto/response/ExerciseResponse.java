package com.fitness.fitnesstrackerapi.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExerciseResponse {
    private String name;

    private String description;
}
