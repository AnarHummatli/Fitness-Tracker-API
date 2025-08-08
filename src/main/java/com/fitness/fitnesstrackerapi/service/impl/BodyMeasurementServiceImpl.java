package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.ResourceNotFoundException;
import com.fitness.fitnesstrackerapi.model.dto.request.BodyMeasurementRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.BodyMeasurementResponse;
import com.fitness.fitnesstrackerapi.model.dto.response.CompareResponse;
import com.fitness.fitnesstrackerapi.model.dto.response.ProgressResponse;
import com.fitness.fitnesstrackerapi.model.entity.BodyMeasurement;
import com.fitness.fitnesstrackerapi.model.entity.User;
import com.fitness.fitnesstrackerapi.repository.BodyMeasurementRepository;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.service.BodyMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BodyMeasurementServiceImpl implements BodyMeasurementService {

    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final UserRepository userRepository;

    @Override
    public BodyMeasurementResponse addMeasurement(BodyMeasurementRequest request) {
        User user = getCurrentUser();

        if (request.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future.");
        }

        if (bodyMeasurementRepository.existsByUserAndDateAndPart(user, request.getDate(), request.getPart())) {
            throw new IllegalArgumentException("Measurement for this part and date already exists.");
        }

        BodyMeasurement measurement = BodyMeasurement.builder()
                .user(user)
                .date(request.getDate())
                .part(request.getPart())
                .value(request.getValue())
                .build();

        BodyMeasurement saved = bodyMeasurementRepository.save(measurement);

        return new BodyMeasurementResponse(
                saved.getId(),
                saved.getDate(),
                saved.getPart(),
                saved.getValue()
        );

    }

    @Override
    public List<BodyMeasurementResponse> getAllMeasurements() {
        User user = getCurrentUser();

        List<BodyMeasurement> measurements = bodyMeasurementRepository
                .findAllByUserOrderByDateDesc(user);

        return measurements.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<BodyMeasurementResponse> getAllMeasurementsForPart(String part) {
        User user = getCurrentUser();
        String normalizedPart = normalizePart(part);

        List<BodyMeasurement> measurements = bodyMeasurementRepository
                .findAllByUserAndPartOrderByDateDesc(user, normalizedPart);

        return measurements.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<BodyMeasurementResponse> getMeasurementsForDate(LocalDate date) {
        User user = getCurrentUser();

        List<BodyMeasurement> measurements = bodyMeasurementRepository
                .findAllByUserAndDate(user, date);

        return measurements.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<BodyMeasurementResponse> getAllMeasurementsForPartBetweenDates(String part, LocalDate from, LocalDate to) {
        User user = getCurrentUser();
        String normalizedPart = normalizePart(part);

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        List<BodyMeasurement> measurements = bodyMeasurementRepository
                .findAllByUserAndPartAndDateBetweenOrderByDateAsc(user, normalizedPart, from, to);

        return measurements.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProgressResponse getProgress(String part) {
        User user = getCurrentUser();
        String normalizedPart = normalizePart(part);

        List<BodyMeasurement> measurements = bodyMeasurementRepository
                .findAllByUserAndPartOrderByDateAsc(user, normalizedPart);

        if (measurements.isEmpty()) {
            throw new ResourceNotFoundException("BodyMeasurement", "part", part);
        }

        BodyMeasurement first = measurements.get(0);
        BodyMeasurement last = measurements.get(measurements.size() - 1);

        double startValue = first.getValue();
        double currentValue = last.getValue();
        double difference = currentValue - startValue;

        long daysTracked = java.time.temporal.ChronoUnit.DAYS.between(first.getDate(), last.getDate());
        double averageChangePerDay = daysTracked > 0 ? difference / daysTracked : 0.0;
        double percentageChange = startValue != 0 ? (difference / startValue) * 100 : 0.0;

        return new ProgressResponse(
                normalizedPart,
                startValue,
                currentValue,
                difference,
                percentageChange,
                daysTracked,
                averageChangePerDay
        );
    }

    @Override
    public CompareResponse compareMeasurementsInRange(String part, LocalDate from, LocalDate to) {
        User user = getCurrentUser();
        String normalizedPart = normalizePart(part);

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        List<BodyMeasurement> measurements = bodyMeasurementRepository
                .findAllByUserAndPartAndDateBetweenOrderByDateAsc(user, normalizedPart, from, to);

        if (measurements.isEmpty()) {
            throw new ResourceNotFoundException("BodyMeasurement", "range", normalizedPart);
        }

        BodyMeasurement first = measurements.get(0);
        BodyMeasurement last = measurements.get(measurements.size() - 1);

        double startValue = first.getValue();
        double endValue = last.getValue();
        double difference = endValue - startValue;

        long daysTracked = java.time.temporal.ChronoUnit.DAYS.between(first.getDate(), last.getDate());
        double avgChangePerDay = daysTracked > 0 ? difference / daysTracked : 0.0;
        double percentageChange = startValue != 0 ? (difference / startValue) * 100 : 0.0;

        return new CompareResponse(
                normalizedPart,
                startValue,
                endValue,
                difference,
                percentageChange,
                daysTracked,
                avgChangePerDay
        );
    }

    @Override
    public void deleteMeasurement(Long id) {
        User user = getCurrentUser();
        BodyMeasurement measurement = bodyMeasurementRepository.findById(id)
                .filter(m -> m.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("BodyMeasurement", "id", id));

        bodyMeasurementRepository.delete(measurement);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private BodyMeasurementResponse toResponse(BodyMeasurement m) {
        return new BodyMeasurementResponse(
                m.getId(),
                m.getDate(),
                m.getPart(),
                m.getValue()
        );
    }


    private String normalizePart(String part) {
        return part.trim().toLowerCase();
    }

}
