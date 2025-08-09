package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.request.DailyStepGoalRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.DailyStepGoalResponse;
import com.fitness.fitnesstrackerapi.model.entity.DailyStepGoal;
import com.fitness.fitnesstrackerapi.model.entity.User;
import com.fitness.fitnesstrackerapi.repository.DailyStepGoalRepository;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.service.DailyStepGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyStepGoalServiceImpl implements DailyStepGoalService {

    private final DailyStepGoalRepository dailyStepGoalRepository;
    private final UserRepository userRepository;

    @Override
    public DailyStepGoalResponse createDailyStepGoal(DailyStepGoalRequest request) {
        User user = getCurrentUser();

        DailyStepGoal goal = DailyStepGoal.builder()
                .user(user)
                .dailyStepGoal(request.getDailyStepGoal())
                .startDate(request.getStartDate())
                .durationDays(request.getDurationDays())
                .build();

        dailyStepGoalRepository.save(goal);

        return mapToResponse(goal);
    }

    @Override
    public DailyStepGoalResponse updateDailyStepGoal(Long id, DailyStepGoalRequest request) {
        User user = getCurrentUser();

        DailyStepGoal goal = dailyStepGoalRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("DailyStepGoal", "id", id));

        goal.setDailyStepGoal(request.getDailyStepGoal());
        goal.setStartDate(request.getStartDate());
        goal.setDurationDays(request.getDurationDays());

        dailyStepGoalRepository.save(goal);

        return mapToResponse(goal);
    }

    @Override
    public DailyStepGoalResponse getDailyStepGoalById(Long id) {
        User user = getCurrentUser();

        DailyStepGoal goal = dailyStepGoalRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("DailyStepGoal", "id", id));

        return mapToResponse(goal);
    }

    @Override
    public List<DailyStepGoalResponse> getAllDailyStepGoals() {
        User user = getCurrentUser();

        List<DailyStepGoal> goals = dailyStepGoalRepository.findByUser(user);

        return goals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<DailyStepGoalResponse> getDailyStepGoalByDate(LocalDate date) {
        User user = getCurrentUser();

        List<DailyStepGoal> activeGoals = dailyStepGoalRepository.findByUser(user).stream()
                .filter(goal -> {
                    LocalDate endDate = goal.getStartDate().plusDays(goal.getDurationDays() - 1);
                    return !date.isBefore(goal.getStartDate()) && !date.isAfter(endDate);
                })
                .toList();

        if (activeGoals.isEmpty()) {
            throw new ResourceNotFoundException("DailyStepGoal", "date", date.toString());
        }

        return activeGoals.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public void deleteDailyStepGoal(Long id) {
        User user = getCurrentUser();

        DailyStepGoal goal = dailyStepGoalRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("DailyStepGoal", "id", id));

        dailyStepGoalRepository.delete(goal);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private DailyStepGoalResponse mapToResponse(DailyStepGoal goal) {
        return DailyStepGoalResponse.builder()
                .id(goal.getId())
                .dailyStepGoal(goal.getDailyStepGoal())
                .startDate(goal.getStartDate())
                .durationDays(goal.getDurationDays())
                .endDate(goal.getStartDate().plusDays(goal.getDurationDays() - 1))
                .build();
    }
}
