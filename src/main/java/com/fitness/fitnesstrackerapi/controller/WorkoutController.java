package com.fitness.fitnesstrackerapi.controller;

import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionRequest;
import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionResponse;
import com.fitness.fitnesstrackerapi.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("/create")
    public ResponseEntity<WorkoutSessionResponse> createWorkoutSession(@RequestBody @Valid WorkoutSessionRequest request) {
        WorkoutSessionResponse response = workoutService.createWorkoutSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<WorkoutSessionResponse> completeWorkout(@PathVariable Long id) {
        WorkoutSessionResponse response = workoutService.markWorkoutAsCompleted(id);
        return ResponseEntity.ok(response);
    }
}
