package com.fitness.fitnesstrackerapi.repository;

import com.fitness.fitnesstrackerapi.model.entity.Goal;
import com.fitness.fitnesstrackerapi.model.entity.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findByUserIdAndType(Long userId, String type);
    List<Goal> findByUserId(Long userId);
    List<Goal> findByUserIdAndStatus(Long userId, GoalStatus status);
}
