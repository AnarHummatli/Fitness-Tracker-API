package com.fitness.fitnesstrackerapi.controller;

import com.fitness.fitnesstrackerapi.model.dto.request.ExerciseRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.ExerciseResponse;
import com.fitness.fitnesstrackerapi.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(@RequestBody @Valid ExerciseRequest request){
        ExerciseResponse response = exerciseService.createExercise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> getAllExercises(){
        List<ExerciseResponse> responses = exerciseService.getAllExercises();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteExercise(@PathVariable String name){
        exerciseService.deleteExerciseByName(name);
        return ResponseEntity.noContent().build();
    }
}
