package com.fitness.fitnesstrackerapi.repository;

import com.fitness.fitnesstrackerapi.model.entity.BodyMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, Long> {

}
