package com.fitness.fitnesstrackerapi.controller;

import com.fitness.fitnesstrackerapi.model.dto.request.GoalRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.GoalResponse;
import com.fitness.fitnesstrackerapi.model.entity.GoalStatus;
import com.fitness.fitnesstrackerapi.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(@RequestBody @Valid GoalRequest request) {
        GoalResponse createdGoal = goalService.createGoal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> getGoalById(@PathVariable Long id) {
        GoalResponse goal = goalService.getGoalById(id);
        return ResponseEntity.ok(goal);
    }

    @GetMapping
    public ResponseEntity<List<GoalResponse>> getAllGoals() {
        List<GoalResponse> goals = goalService.getAllGoals();
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<GoalResponse>> getGoalsByStatus(@PathVariable GoalStatus status) {
        List<GoalResponse> goals = goalService.getGoalsByStatus(status);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<GoalResponse> getGoalByType(@PathVariable String type) {
        GoalResponse goal = goalService.getGoalByType(type);
        return ResponseEntity.ok(goal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> updateGoal(@PathVariable Long id, @RequestBody @Valid GoalRequest request) {
        GoalResponse updatedGoal = goalService.updateGoal(id, request);
        return ResponseEntity.ok(updatedGoal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
