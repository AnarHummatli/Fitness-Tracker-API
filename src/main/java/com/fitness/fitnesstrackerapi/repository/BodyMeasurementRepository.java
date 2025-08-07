package com.fitness.fitnesstrackerapi.repository;

import com.fitness.fitnesstrackerapi.model.entity.BodyMeasurement;
import com.fitness.fitnesstrackerapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, Long> {
    boolean existsByUserAndDateAndPart(User user, LocalDate date, String part);
    List<BodyMeasurement> findAllByUserAndDate(User user, LocalDate date);
    List<BodyMeasurement> findAllByUserAndPartOrderByDateAsc(User user, String part);
    List<BodyMeasurement> findAllByUserAndPartAndDateBetweenOrderByDateAsc(User user, String part, LocalDate from, LocalDate to);
    Optional<BodyMeasurement> findFirstByUserAndPartOrderByDateAsc(User user, String part);
    Optional<BodyMeasurement> findFirstByUserAndPartOrderByDateDesc(User user, String part);
}
