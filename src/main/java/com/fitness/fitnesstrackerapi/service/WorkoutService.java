package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionRequest;
import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionResponse;

public interface WorkoutService {
    WorkoutSessionResponse createWorkoutSession(WorkoutSessionRequest request);
    WorkoutSessionResponse markWorkoutAsCompleted(Long sessionId);
}
