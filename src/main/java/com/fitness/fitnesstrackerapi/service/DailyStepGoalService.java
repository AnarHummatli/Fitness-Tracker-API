package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.request.DailyStepGoalRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.DailyStepGoalResponse;

import java.time.LocalDate;
import java.util.List;

public interface DailyStepGoalService {
    DailyStepGoalResponse createDailyStepGoal(DailyStepGoalRequest request);
    DailyStepGoalResponse updateDailyStepGoal(Long id, DailyStepGoalRequest request);
    DailyStepGoalResponse getDailyStepGoalById(Long id);
    List<DailyStepGoalResponse> getAllDailyStepGoals();
    List<DailyStepGoalResponse> getDailyStepGoalByDate(LocalDate date);
    void deleteDailyStepGoal(Long id);
}
