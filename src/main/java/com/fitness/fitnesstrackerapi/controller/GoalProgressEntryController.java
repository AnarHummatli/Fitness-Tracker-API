package com.fitness.fitnesstrackerapi.controller;

import com.fitness.fitnesstrackerapi.model.dto.request.GoalProgressEntryRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.GoalProgressEntryResponse;
import com.fitness.fitnesstrackerapi.service.GoalProgressEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/progress-entries")
@RequiredArgsConstructor
public class GoalProgressEntryController {

    private final GoalProgressEntryService progressEntryService;

    @PostMapping
    public ResponseEntity<GoalProgressEntryResponse> addProgressEntry(@RequestBody @Valid GoalProgressEntryRequest request) {
        GoalProgressEntryResponse response = progressEntryService.addProgressEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GoalProgressEntryResponse>> getAllProgressEntries() {
        List<GoalProgressEntryResponse> entries = progressEntryService.getAllProgressEntries();
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/goal/{goalId}")
    public ResponseEntity<List<GoalProgressEntryResponse>> getEntriesByGoalId(@PathVariable Long goalId) {
        List<GoalProgressEntryResponse> responses = progressEntryService.getProgressEntriesByGoalId(goalId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<GoalProgressEntryResponse>> getEntriesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<GoalProgressEntryResponse> responses = progressEntryService.getProgressEntriesByGoalDate(date);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgressEntry(@PathVariable Long id) {
        progressEntryService.deleteProgressEntry(id);
        return ResponseEntity.noContent().build();
    }
}
