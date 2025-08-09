package com.fitness.fitnesstrackerapi.controller;

import com.fitness.fitnesstrackerapi.model.dto.request.DailyStepGoalRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.DailyStepGoalResponse;
import com.fitness.fitnesstrackerapi.service.DailyStepGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dailyStepGoals")
@RequiredArgsConstructor
public class DailyStepGoalController {

    private final DailyStepGoalService dailyStepGoalService;

    @PostMapping
    public ResponseEntity<DailyStepGoalResponse> create(@RequestBody @Valid DailyStepGoalRequest request) {
        DailyStepGoalResponse response = dailyStepGoalService.createDailyStepGoal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyStepGoalResponse> update(@PathVariable Long id, @RequestBody @Valid DailyStepGoalRequest request) {
        DailyStepGoalResponse response = dailyStepGoalService.updateDailyStepGoal(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailyStepGoalResponse> getById(@PathVariable Long id) {
        DailyStepGoalResponse response = dailyStepGoalService.getDailyStepGoalById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DailyStepGoalResponse>> getAll() {
        List<DailyStepGoalResponse> responses = dailyStepGoalService.getAllDailyStepGoals();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/byDate/{date}")
    public ResponseEntity<List<DailyStepGoalResponse>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DailyStepGoalResponse> responses = dailyStepGoalService.getDailyStepGoalByDate(date);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dailyStepGoalService.deleteDailyStepGoal(id);
        return ResponseEntity.noContent().build();
    }
}
