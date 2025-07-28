package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.WorkoutEntryResponse;
import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionRequest;
import com.fitness.fitnesstrackerapi.model.dto.WorkoutSessionResponse;
import com.fitness.fitnesstrackerapi.model.entity.*;
import com.fitness.fitnesstrackerapi.repository.ExerciseRepository;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.repository.WorkoutEntryRepository;
import com.fitness.fitnesstrackerapi.repository.WorkoutSessionRepository;
import com.fitness.fitnesstrackerapi.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutEntryRepository workoutEntryRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public WorkoutSessionResponse createWorkoutSession(WorkoutSessionRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));

        WorkoutSessionStatus status;
        LocalDate today = LocalDate.now();
        LocalDate date = request.getDate();

        if (date.isBefore(today)) {
            status = WorkoutSessionStatus.MISSED;
        } else {
            status = WorkoutSessionStatus.PLANNED;
        }

        WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setUser(user);
        workoutSession.setDate(date);
        workoutSession.setWorkoutSessionStatus(status);

        WorkoutSession savedSession = workoutSessionRepository.save(workoutSession);

        List<WorkoutEntry> entries = request.getEntries().stream().map(workoutEntryRequest -> {
            Exercise exercise = exerciseRepository.findByNameAndUser(workoutEntryRequest.getExerciseName(), user)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Exercise", workoutEntryRequest.getExerciseName()));

            return WorkoutEntry.builder()
                    .workoutSession(savedSession)
                    .exercise(exercise)
                    .sets(workoutEntryRequest.getSets())
                    .reps(workoutEntryRequest.getReps())
                    .weight(workoutEntryRequest.getWeight())
                    .build();
        }).toList();

        workoutEntryRepository.saveAll(entries);
        savedSession.setWorkoutEntries(entries);

        List<WorkoutEntryResponse> workoutEntryResponses = entries.stream().map(entry ->
                new WorkoutEntryResponse(
                        entry.getExercise().getName(),
                        entry.getSets(),
                        entry.getReps(),
                        entry.getWeight()
                )
        ).toList();

        return new WorkoutSessionResponse(
                savedSession.getId(),
                savedSession.getDate(),
                savedSession.getWorkoutSessionStatus(),
                workoutEntryResponses
        );

    }
}
