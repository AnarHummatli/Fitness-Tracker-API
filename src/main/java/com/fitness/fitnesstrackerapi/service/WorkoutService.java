package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionRequest;
import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionResponse;
import com.fitness.fitnesstrackerapi.model.entity.WorkoutSessionStatus;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutService {
    WorkoutSessionResponse createWorkoutSession(WorkoutSessionRequest request);
    WorkoutSessionResponse markWorkoutAsCompleted(Long sessionId);
    List<WorkoutSessionResponse> getWorkoutsByDate(LocalDate date);
    List<WorkoutSessionResponse> getWorkoutsByStatus(WorkoutSessionStatus status);
    void deleteWorkoutById(Long sessionId);
    List<WorkoutSessionResponse> getAllWorkouts();
}
