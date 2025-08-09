package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.response.StepStreakResponse;
import com.fitness.fitnesstrackerapi.model.entity.StepStreak;
import com.fitness.fitnesstrackerapi.model.entity.User;
import com.fitness.fitnesstrackerapi.repository.StepStreakRepository;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.service.StepStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StepStreakServiceImpl implements StepStreakService {

    private final StepStreakRepository stepStreakRepository;
    private final UserRepository userRepository;

    @Override
    public StepStreakResponse getUserStreak() {
        User user = getCurrentUser();

        StepStreak streak = stepStreakRepository.findByUser(user)
                .orElseGet(() -> StepStreak.builder()
                        .user(user)
                        .currentStreak(0)
                        .longestStreak(0)
                        .build());

        return mapToResponse(streak);
    }

    @Override
    public void resetStreak() {
        User user = getCurrentUser();

        StepStreak streak = stepStreakRepository.findByUser(user)
                .orElseGet(() -> StepStreak.builder()
                        .user(user)
                        .currentStreak(0)
                        .longestStreak(0)
                        .build());

        streak.setCurrentStreak(0);
        stepStreakRepository.save(streak);
    }


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private StepStreakResponse mapToResponse(StepStreak streak) {
        return StepStreakResponse.builder()
                .currentStreak(streak.getCurrentStreak())
                .longestStreak(streak.getLongestStreak())
                .build();
    }
}
