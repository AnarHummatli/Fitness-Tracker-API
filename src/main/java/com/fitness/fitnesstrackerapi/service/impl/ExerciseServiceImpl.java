package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.ExerciseRequest;
import com.fitness.fitnesstrackerapi.model.dto.ExerciseResponse;
import com.fitness.fitnesstrackerapi.model.entity.Exercise;
import com.fitness.fitnesstrackerapi.model.entity.User;
import com.fitness.fitnesstrackerapi.repository.ExerciseRepository;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Override
    public ExerciseResponse createExercise(ExerciseRequest request) {

        User user = getCurrentUser();

        Exercise exercise = new Exercise();
        exercise.setName(request.getName());
        exercise.setDescription(request.getDescription());
        exercise.setUser(user);

        Exercise savedExercise = exerciseRepository.save(exercise);

        return new ExerciseResponse(savedExercise.getName(), savedExercise.getDescription());
    }

    @Override
    public List<ExerciseResponse> getAllExercises() {

        User user = getCurrentUser();

        return exerciseRepository.findAllByUser(user)
                .stream()
                .map(response -> new ExerciseResponse(response.getName(),response.getDescription()))
                .toList();
    }

    @Override
    public void deleteExerciseByName(String name) {

        User user = getCurrentUser();

        Exercise exercise = exerciseRepository.findByNameAndUser(name, user)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise", name));

        exerciseRepository.delete(exercise);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
