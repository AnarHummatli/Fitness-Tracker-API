package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.request.GoalRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.GoalResponse;
import com.fitness.fitnesstrackerapi.model.entity.GoalStatus;

import java.util.List;

public interface GoalService {
    GoalResponse createGoal(GoalRequest request);
    GoalResponse getGoalById(Long id);
    List<GoalResponse> getAllGoals();
    List<GoalResponse> getGoalsByStatus(GoalStatus status);
    GoalResponse getGoalByType(String type);
    GoalResponse updateGoal(Long id, GoalRequest request);
    void deleteGoal(Long id);
}
