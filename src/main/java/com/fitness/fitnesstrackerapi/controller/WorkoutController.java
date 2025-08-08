package com.fitness.fitnesstrackerapi.controller;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionRequest;
import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionResponse;
import com.fitness.fitnesstrackerapi.model.entity.WorkoutSessionStatus;
import com.fitness.fitnesstrackerapi.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<WorkoutSessionResponse>> getAllWorkouts() {
        List<WorkoutSessionResponse> responses = workoutService.getAllWorkouts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<WorkoutSessionResponse>> getWorkoutsByDate(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        List<WorkoutSessionResponse> responses = workoutService.getWorkoutsByDate(parsedDate);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<WorkoutSessionResponse>> getWorkoutsByStatus(@PathVariable String status){
        WorkoutSessionStatus enumStatus;
        try {
            enumStatus = WorkoutSessionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("WorkoutSessionStatus", "status", status);
        }
        List<WorkoutSessionResponse> responses = workoutService.getWorkoutsByStatus(enumStatus);
        return ResponseEntity.ok(responses);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkoutById(id);
        return ResponseEntity.noContent().build();
    }
}
