package com.fitness.fitnesstrackerapi.controller;

import com.fitness.fitnesstrackerapi.model.dto.request.StepCountRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.StepCountResponse;
import com.fitness.fitnesstrackerapi.service.StepCountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/steps")
@RequiredArgsConstructor
public class StepCountController {

    private final StepCountService stepCountService;

    @PostMapping
    public ResponseEntity<StepCountResponse> add(@RequestBody @Valid StepCountRequest request) {
        StepCountResponse response = stepCountService.addStepCount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StepCountResponse> update(@PathVariable Long id, @RequestBody @Valid StepCountRequest request) {
        StepCountResponse response = stepCountService.updateStepCount(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{date}")
    public ResponseEntity<StepCountResponse> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        StepCountResponse response = stepCountService.getStepCountByDate(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StepCountResponse>> getRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<StepCountResponse> responses = stepCountService.getStepCountsByDateRange(start, end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/totalSteps")
    public ResponseEntity<Long> totalSteps(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        Long totalSteps = stepCountService.getTotalStepsByDateRange(start, end);
        return ResponseEntity.ok(totalSteps);
    }

    @GetMapping("/totalCalories")
    public ResponseEntity<Double> totalCalories(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        Double totalCalories = stepCountService.getTotalCaloriesByDateRange(start, end);
        return ResponseEntity.ok(totalCalories);
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<Void> delete(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        stepCountService.deleteStepCount(date);
        return ResponseEntity.noContent().build();
    }
}