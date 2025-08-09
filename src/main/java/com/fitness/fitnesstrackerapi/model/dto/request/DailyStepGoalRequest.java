package com.fitness.fitnesstrackerapi.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DailyStepGoalRequest {

    @NotNull
    @Min(1)
    private Integer dailyStepGoal;

    @NotNull
    private LocalDate startDate;

    @NotNull
    @Min(1)
    private Integer durationDays;
}
