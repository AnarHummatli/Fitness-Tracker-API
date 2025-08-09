package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.request.StepCountRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.StepCountResponse;
import com.fitness.fitnesstrackerapi.model.entity.*;
import com.fitness.fitnesstrackerapi.repository.DailyStepGoalRepository;
import com.fitness.fitnesstrackerapi.repository.StepCountRepository;
import com.fitness.fitnesstrackerapi.repository.StepStreakRepository;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.service.StepCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StepCountServiceImpl implements StepCountService {

    private final StepCountRepository stepCountRepository;
    private final DailyStepGoalRepository dailyStepGoalRepository;
    private final StepStreakRepository stepStreakRepository;
    private final UserRepository userRepository;

    @Override
    public StepCountResponse addStepCount(StepCountRequest request) {
        User user = getCurrentUser();

        if (request.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future.");
        }

        boolean exists = stepCountRepository.existsByUserAndDate(user, request.getDate());
        if (exists) {
            throw new IllegalArgumentException("Step count for this date already exists.");
        }

        StepCount stepCount = StepCount.builder()
                .user(user)
                .date(request.getDate())
                .stepCount(request.getStepCount())
                .status(calculateStatus(user, request.getDate(), request.getStepCount()))
                .build();

        stepCountRepository.save(stepCount);

        updateStepStreak(user);

        return mapToResponse(stepCount);
    }

    @Override
    public StepCountResponse updateStepCount(Long id, StepCountRequest request) {
        User user = getCurrentUser();

        if (request.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future.");
        }

        StepCount stepCount = stepCountRepository.findById(id)
                .filter(sc -> sc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("StepCount", "id", id));

        stepCount.setStepCount(request.getStepCount());
        stepCount.setStatus(calculateStatus(user, stepCount.getDate(), request.getStepCount()));

        stepCountRepository.save(stepCount);

        updateStepStreak(user);

        return mapToResponse(stepCount);
    }

    @Override
    public StepCountResponse getStepCountByDate(LocalDate date) {
        User user = getCurrentUser();

        StepCount stepCount = stepCountRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new ResourceNotFoundException("StepCount", "date", date.toString()));

        return mapToResponse(stepCount);
    }

    @Override
    public List<StepCountResponse> getStepCountsByDateRange(LocalDate startDate, LocalDate endDate) {
        User user = getCurrentUser();

        return stepCountRepository.findByUserAndDateBetween(user, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public long getTotalStepsByDateRange(LocalDate startDate, LocalDate endDate) {
        User user = getCurrentUser();

        return stepCountRepository.findByUserAndDateBetween(user, startDate, endDate)
                .stream()
                .mapToLong(StepCount::getStepCount)
                .sum();
    }

    @Override
    public double getTotalCaloriesByDateRange(LocalDate startDate, LocalDate endDate) {
        User user = getCurrentUser();

        return stepCountRepository.findByUserAndDateBetween(user, startDate, endDate)
                .stream()
                .mapToDouble(StepCount::getCaloriesBurned)
                .sum();
    }

    @Override
    public void deleteStepCount(LocalDate date) {
        User user = getCurrentUser();

        StepCount stepCount = stepCountRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new ResourceNotFoundException("StepCount", "date", date.toString()));

        stepCountRepository.delete(stepCount);

        updateStepStreak(user);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private StepGoalStatus calculateStatus(User user, LocalDate date, Integer stepCount) {
        Optional<DailyStepGoal> activeGoal = dailyStepGoalRepository.findByUser(user).stream()
                .filter(goal -> {
                    LocalDate endDate = goal.getStartDate().plusDays(goal.getDurationDays() - 1);
                    return !date.isBefore(goal.getStartDate()) && !date.isAfter(endDate);
                })
                .findFirst();

        if (activeGoal.isEmpty()) {
            return StepGoalStatus.NO_GOAL;
        }

        return stepCount >= activeGoal.get().getDailyStepGoal() ? StepGoalStatus.COMPLETED : StepGoalStatus.IN_PROGRESS;
    }

    private void updateStepStreak(User user) {
        StepStreak streak = stepStreakRepository.findByUser(user)
                .orElseGet(() -> StepStreak.builder()
                        .user(user)
                        .currentStreak(0)
                        .longestStreak(0)
                        .build());

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        boolean yesterdayCompleted = stepCountRepository.findByUserAndDate(user, yesterday)
                .map(sc -> sc.getStatus() == StepGoalStatus.COMPLETED)
                .orElse(false);

        boolean todayCompleted = stepCountRepository.findByUserAndDate(user, today)
                .map(sc -> sc.getStatus() == StepGoalStatus.COMPLETED)
                .orElse(false);

        if (todayCompleted) {
            int newCurrentStreak = yesterdayCompleted ? streak.getCurrentStreak() + 1 : 1;
            streak.setCurrentStreak(newCurrentStreak);
            streak.setLongestStreak(Math.max(streak.getLongestStreak(), newCurrentStreak));
        } else if (!yesterdayCompleted) {
            streak.setCurrentStreak(0);
        }

        stepStreakRepository.save(streak);
    }

    private StepCountResponse mapToResponse(StepCount stepCount) {
        return StepCountResponse.builder()
                .id(stepCount.getId())
                .date(stepCount.getDate())
                .stepCount(stepCount.getStepCount())
                .caloriesBurned(stepCount.getCaloriesBurned())
                .status(stepCount.getStatus())
                .build();
    }
}
