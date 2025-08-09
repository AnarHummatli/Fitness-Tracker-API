package com.fitness.fitnesstrackerapi.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DailyStepGoalResponse {
    private Long id;
    private Integer dailyStepGoal;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationDays;
}
