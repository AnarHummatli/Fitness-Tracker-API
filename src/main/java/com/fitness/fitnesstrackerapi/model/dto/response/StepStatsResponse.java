package com.fitness.fitnesstrackerapi.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StepStatsResponse {
    private Integer totalSteps;
    private Double totalCalories;
}
