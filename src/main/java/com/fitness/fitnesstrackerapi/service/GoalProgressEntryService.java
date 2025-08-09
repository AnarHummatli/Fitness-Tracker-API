package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.request.GoalProgressEntryRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.GoalProgressEntryResponse;

import java.time.LocalDate;
import java.util.List;

public interface GoalProgressEntryService {
    GoalProgressEntryResponse addProgressEntry(GoalProgressEntryRequest request);
    List<GoalProgressEntryResponse> getProgressEntriesByGoalId(Long goalId);
    List<GoalProgressEntryResponse> getProgressEntriesByGoalDate(LocalDate date);
    void deleteProgressEntry(Long id);
}
