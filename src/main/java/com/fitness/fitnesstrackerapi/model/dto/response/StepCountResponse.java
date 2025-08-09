package com.fitness.fitnesstrackerapi.model.dto.response;

import com.fitness.fitnesstrackerapi.model.entity.StepGoalStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StepCountResponse {
    private Long id;
    private LocalDate date;
    private Integer stepCount;
    private Double caloriesBurned;
    private StepGoalStatus status;
}
