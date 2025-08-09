package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.response.StepStreakResponse;

public interface StepStreakService {
    StepStreakResponse getUserStreak();
    void resetStreak();
}
