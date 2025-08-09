package com.fitness.fitnesstrackerapi.controller;

import com.fitness.fitnesstrackerapi.model.dto.response.StepStreakResponse;
import com.fitness.fitnesstrackerapi.service.StepStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stepStreak")
@RequiredArgsConstructor
public class StepStreakController {

    private final StepStreakService stepStreakService;

    @GetMapping
    public ResponseEntity<StepStreakResponse> get() {
        StepStreakResponse response = stepStreakService.getUserStreak();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        stepStreakService.resetStreak();
        return ResponseEntity.noContent().build();
    }
}