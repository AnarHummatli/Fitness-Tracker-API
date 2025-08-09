package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.request.GoalRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.GoalResponse;
import com.fitness.fitnesstrackerapi.model.entity.Goal;
import com.fitness.fitnesstrackerapi.model.entity.GoalStatus;
import com.fitness.fitnesstrackerapi.model.entity.User;
import com.fitness.fitnesstrackerapi.repository.GoalRepository;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.service.GoalService;
import com.fitness.fitnesstrackerapi.util.GoalCalculation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Override
    public GoalResponse createGoal(GoalRequest request) {
        User currentUser = getCurrentUser();

        goalRepository.findByUserIdAndType(currentUser.getId(), request.getType())
                .ifPresent(g -> {
                    throw new IllegalArgumentException(
                            "Goal with type '" + request.getType() + "' already exists for this user.");
                });

        Goal goal = Goal.builder()
                .user(currentUser)
                .type(request.getType().trim())
                .startValue(request.getStartValue())
                .targetValue(request.getTargetValue())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(GoalStatus.IN_PROGRESS)
                .build();

        GoalCalculation.calculateGoalMetrics(goal);

        Goal savedGoal = goalRepository.save(goal);
        return mapToGoalResponse(savedGoal);
    }

    @Override
    public GoalResponse getGoalById(Long id) {
        User currentUser = getCurrentUser();

        Goal goal = goalRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));

        GoalCalculation.calculateGoalMetrics(goal);

        return mapToGoalResponse(goal);
    }

    @Override
    public List<GoalResponse> getAllGoals() {
        User currentUser = getCurrentUser();

        List<Goal> goals = goalRepository.findByUserId(currentUser.getId());

        goals.forEach(GoalCalculation::calculateGoalMetrics);

        return goals.stream()
                .map(this::mapToGoalResponse)
                .toList();
    }

    @Override
    public List<GoalResponse> getGoalsByStatus(GoalStatus status) {
        User currentUser = getCurrentUser();

        List<Goal> goals = goalRepository.findByUserIdAndStatus(currentUser.getId(), status);

        goals.forEach(GoalCalculation::calculateGoalMetrics);

        return goals.stream()
                .map(this::mapToGoalResponse)
                .toList();
    }

    @Override
    public GoalResponse getGoalByType(String type) {
        User currentUser = getCurrentUser();

        Goal goal = goalRepository.findByUserIdAndType(currentUser.getId(), type.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "type", type));

        GoalCalculation.calculateGoalMetrics(goal);

        return mapToGoalResponse(goal);
    }

    @Override
    public GoalResponse updateGoal(Long id, GoalRequest request) {
        User currentUser = getCurrentUser();

        Goal goal = goalRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));

        goalRepository.findByUserIdAndType(currentUser.getId(), request.getType().trim())
                .filter(g -> !g.getId().equals(id))
                .ifPresent(g -> {
                    throw new IllegalArgumentException("Another goal with type '" + request.getType() + "' already exists.");
                });

        goal.setType(request.getType().trim());
        goal.setStartValue(request.getStartValue());
        goal.setTargetValue(request.getTargetValue());
        goal.setStartDate(request.getStartDate());
        goal.setEndDate(request.getEndDate());

        GoalCalculation.calculateGoalMetrics(goal);

        Goal updatedGoal = goalRepository.save(goal);
        return mapToGoalResponse(updatedGoal);
    }

    @Override
    public void deleteGoal(Long id) {
        User currentUser = getCurrentUser();

        Goal goal = goalRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));

        goalRepository.delete(goal);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private GoalResponse mapToGoalResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .status(goal.getStatus())
                .type(goal.getType())
                .startValue(goal.getStartValue())
                .targetValue(goal.getTargetValue())
                .currentValue(goal.getCurrentValue())
                .progressPercent(goal.getProgressPercent())
                .goalSuccessPercent(goal.getGoalSuccessPercent())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .daysPassed(goal.getDaysPassed())
                .totalDays(goal.getTotalDays())
                .timePassedPercent(goal.getTimePassedPercent())
                .build();
    }

}
