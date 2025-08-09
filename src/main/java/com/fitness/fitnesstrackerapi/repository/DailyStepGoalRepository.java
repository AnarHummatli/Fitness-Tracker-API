package com.fitness.fitnesstrackerapi.repository;

import com.fitness.fitnesstrackerapi.model.entity.DailyStepGoal;
import com.fitness.fitnesstrackerapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyStepGoalRepository extends JpaRepository<DailyStepGoal, Long> {
    List<DailyStepGoal> findByUser(User user);
}
