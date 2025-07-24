package com.fitness.fitnesstrackerapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkoutEntryResponse {

    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Double weight;

}
