package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.request.StepCountRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.StepCountResponse;

import java.time.LocalDate;
import java.util.List;

public interface StepCountService {
    StepCountResponse addStepCount(StepCountRequest request);
    StepCountResponse updateStepCount(Long id, StepCountRequest request);
    StepCountResponse getStepCountByDate(LocalDate date);
    List<StepCountResponse> getStepCountsByDateRange(LocalDate startDate, LocalDate endDate);
    long getTotalStepsByDateRange(LocalDate startDate, LocalDate endDate);
    double getTotalCaloriesByDateRange(LocalDate startDate, LocalDate endDate);
    void deleteStepCount(LocalDate date);
}
