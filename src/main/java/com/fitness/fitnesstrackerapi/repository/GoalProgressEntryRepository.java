package com.fitness.fitnesstrackerapi.repository;

import com.fitness.fitnesstrackerapi.model.entity.GoalProgressEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GoalProgressEntryRepository extends JpaRepository<GoalProgressEntry, Long> {
    List<GoalProgressEntry> findByGoalId(Long goalId);
    List<GoalProgressEntry> findByGoal_UserIdAndDate(Long userId, LocalDate date);
    boolean existsByGoalIdAndDate(Long goalId, LocalDate date);
}
