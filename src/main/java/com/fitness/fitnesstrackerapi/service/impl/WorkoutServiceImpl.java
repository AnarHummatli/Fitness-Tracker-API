package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.response.WorkoutEntryResponse;
import com.fitness.fitnesstrackerapi.model.dto.request.WorkoutSessionRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.WorkoutSessionResponse;
import com.fitness.fitnesstrackerapi.model.entity.*;
import com.fitness.fitnesstrackerapi.repository.ExerciseRepository;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.repository.WorkoutEntryRepository;
import com.fitness.fitnesstrackerapi.repository.WorkoutSessionRepository;
import com.fitness.fitnesstrackerapi.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
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
        User user = getCurrentUser();

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

        List<WorkoutEntry> entries = request.getEntries().stream().map(entry -> {
            Exercise exercise = exerciseRepository.findByNameAndUser(entry.getExerciseName(), user)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Exercise", "name", entry.getExerciseName()));
            return WorkoutEntry.builder()
                    .workoutSession(savedSession)
                    .exercise(exercise)
                    .sets(entry.getSets())
                    .reps(entry.getReps())
                    .weight(entry.getWeight())
                    .build();
        }).toList();

        workoutEntryRepository.saveAll(entries);
        savedSession.setWorkoutEntries(entries);

        return mapToWorkoutSessionResponse(savedSession);
    }

    @Transactional
    @Override
    public WorkoutSessionResponse markWorkoutAsCompleted(Long sessionId) {
        User user = getCurrentUser();

        WorkoutSession session = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutSession", "id", sessionId));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this workout session");
        }

        if (session.getWorkoutSessionStatus() == WorkoutSessionStatus.MISSED) {
            throw new IllegalArgumentException("Cannot complete a missed workout session");
        }

        session.setWorkoutSessionStatus(WorkoutSessionStatus.COMPLETED);
        workoutSessionRepository.save(session);

        return mapToWorkoutSessionResponse(session);
    }

    @Override
    public List<WorkoutSessionResponse> getWorkoutsByDate(LocalDate date) {
        User user = getCurrentUser();
        List<WorkoutSession> sessions = workoutSessionRepository.findByUserAndDate(user, date);

        return sessions.stream().map(this::mapToWorkoutSessionResponse).toList();
    }

    @Override
    public List<WorkoutSessionResponse> getWorkoutsByStatus(WorkoutSessionStatus status) {
        User user = getCurrentUser();
        List<WorkoutSession> sessions = workoutSessionRepository.findByUserAndWorkoutSessionStatus(user, status);

        return sessions.stream()
                .map(this::mapToWorkoutSessionResponse)
                .toList();
    }


    @Transactional
    @Override
    public void deleteWorkoutById(Long sessionId) {
        User user = getCurrentUser();

        WorkoutSession session = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutSession", "id", sessionId));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this workout session");
        }

        workoutEntryRepository.deleteAll(session.getWorkoutEntries());
        workoutSessionRepository.delete(session);
    }

    @Override
    public List<WorkoutSessionResponse> getAllWorkouts() {
        User user = getCurrentUser();
        List<WorkoutSession> sessions = workoutSessionRepository.findByUser(user);

        return sessions.stream().map(this::mapToWorkoutSessionResponse).toList();
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void updateMissedStatusesForAllUsers() {
        LocalDate today = LocalDate.now();
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            List<WorkoutSession> plannedSessions =
                    workoutSessionRepository.findByUserAndWorkoutSessionStatus(user, WorkoutSessionStatus.PLANNED);

            for (WorkoutSession session : plannedSessions) {
                if (session.getDate().isBefore(today)) {
                    session.setWorkoutSessionStatus(WorkoutSessionStatus.MISSED);
                    workoutSessionRepository.save(session);
                }
            }
        }
    }


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private WorkoutSessionResponse mapToWorkoutSessionResponse(WorkoutSession session) {
        List<WorkoutEntryResponse> entryResponses = session.getWorkoutEntries().stream().map(entry ->
                new WorkoutEntryResponse(
                        entry.getExercise().getName(),
                        entry.getSets(),
                        entry.getReps(),
                        entry.getWeight()
                )
        ).toList();

        return new WorkoutSessionResponse(
                session.getId(),
                session.getDate(),
                session.getWorkoutSessionStatus(),
                entryResponses
        );
    }
}
