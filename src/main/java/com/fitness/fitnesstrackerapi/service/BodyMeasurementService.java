package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface BodyMeasurementService {
    BodyMeasurementResponse addMeasurement(BodyMeasurementRequest request);
    List<BodyMeasurementResponse> getMeasurementsForDate(LocalDate date);
    List<BodyMeasurementResponse> getAllMeasurementsForPartBetweenDates(String part, LocalDate from, LocalDate to);
    ProgressResponse getProgress(String part);
    CompareResponse compareMeasurementsInRange(String part, LocalDate from, LocalDate to);
    void deleteMeasurement(Long id);
}
