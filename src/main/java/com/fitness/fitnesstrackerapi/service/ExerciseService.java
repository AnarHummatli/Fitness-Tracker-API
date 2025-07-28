package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.ExerciseRequest;
import com.fitness.fitnesstrackerapi.model.dto.ExerciseResponse;

import java.util.List;

public interface ExerciseService {
    ExerciseResponse createExercise(ExerciseRequest request);
    List<ExerciseResponse> getAllExercises();
    void deleteExerciseByName(String name);
}
