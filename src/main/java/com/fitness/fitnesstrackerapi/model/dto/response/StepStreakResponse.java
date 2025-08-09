package com.fitness.fitnesstrackerapi.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StepStreakResponse {
    private Integer currentStreak;
    private Integer longestStreak;
}
