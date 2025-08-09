package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.request.GoalProgressEntryRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.GoalProgressEntryResponse;
import com.fitness.fitnesstrackerapi.model.entity.Goal;
import com.fitness.fitnesstrackerapi.model.entity.GoalProgressEntry;
import com.fitness.fitnesstrackerapi.model.entity.User;
import com.fitness.fitnesstrackerapi.repository.GoalProgressEntryRepository;
import com.fitness.fitnesstrackerapi.repository.GoalRepository;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.service.GoalProgressEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalProgressEntryServiceImpl implements GoalProgressEntryService {

    private final GoalProgressEntryRepository progressEntryRepository;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Override
    public GoalProgressEntryResponse addProgressEntry(GoalProgressEntryRequest request) {
        User currentUser = getCurrentUser();

        Goal goal = goalRepository.findById(request.getGoalId())
                .filter(g -> g.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", request.getGoalId()));

        if (progressEntryRepository.existsByGoalIdAndDate(goal.getId(), request.getDate())) {
            throw new IllegalArgumentException(
                    "A progress entry for this goal on date " + request.getDate() + " already exists.");
        }

        GoalProgressEntry entry = GoalProgressEntry.builder()
                .goal(goal)
                .date(request.getDate())
                .value(request.getValue())
                .build();

        GoalProgressEntry savedEntry = progressEntryRepository.save(entry);
        return mapToResponse(savedEntry);
    }

    @Override
    public List<GoalProgressEntryResponse> getProgressEntriesByGoalId(Long goalId) {
        User currentUser = getCurrentUser();

        goalRepository.findById(goalId)
                .filter(g -> g.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));

        List<GoalProgressEntry> entries = progressEntryRepository.findByGoalId(goalId);

        return entries.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<GoalProgressEntryResponse> getProgressEntriesByGoalDate(LocalDate date) {
        User currentUser = getCurrentUser();

        List<GoalProgressEntry> entries = progressEntryRepository.findByGoal_UserIdAndDate(currentUser.getId(), date);

        return entries.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public void deleteProgressEntry(Long id) {
        User currentUser = getCurrentUser();

        GoalProgressEntry entry = progressEntryRepository.findById(id)
                .filter(e -> e.getGoal().getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("GoalProgressEntry", "id", id));

        progressEntryRepository.delete(entry);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private GoalProgressEntryResponse mapToResponse(GoalProgressEntry entry) {
        return GoalProgressEntryResponse.builder()
                .id(entry.getId())
                .date(entry.getDate())
                .value(entry.getValue())
                .build();
    }
}
