package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface BodyMeasurementService {
    List<BodyMeasurementResponse> getAllMeasurements();
    List<BodyMeasurementResponse> getAllMeasurementsForPart(String part);
    List<BodyMeasurementResponse> getMeasurementsForDate(LocalDate date);
    List<BodyMeasurementResponse> getAllMeasurementsForPartBetweenDates(String part, LocalDate from, LocalDate to);
    ProgressResponse getProgress(String part);
    CompareResponse compareMeasurementsInRange(String part, LocalDate from, LocalDate to);
    BodyMeasurementResponse addMeasurement(BodyMeasurementRequest request);
    void deleteMeasurement(Long id);
}
