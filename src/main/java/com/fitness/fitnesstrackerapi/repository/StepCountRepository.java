package com.fitness.fitnesstrackerapi.repository;

import com.fitness.fitnesstrackerapi.model.entity.StepCount;
import com.fitness.fitnesstrackerapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StepCountRepository extends JpaRepository<StepCount, Long> {
    Optional<StepCount> findByUserAndDate(User user, LocalDate date);
    List<StepCount> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    boolean existsByUserAndDate(User user, LocalDate date);
}
