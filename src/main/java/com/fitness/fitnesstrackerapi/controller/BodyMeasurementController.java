package com.fitness.fitnesstrackerapi.controller;

import com.fitness.fitnesstrackerapi.model.dto.request.BodyMeasurementRequest;
import com.fitness.fitnesstrackerapi.model.dto.response.BodyMeasurementResponse;
import com.fitness.fitnesstrackerapi.model.dto.response.CompareResponse;
import com.fitness.fitnesstrackerapi.model.dto.response.ProgressResponse;
import com.fitness.fitnesstrackerapi.service.BodyMeasurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/body-measurements")
@RequiredArgsConstructor
public class BodyMeasurementController {

    private final BodyMeasurementService bodyMeasurementService;

    @PostMapping
    public ResponseEntity<BodyMeasurementResponse> addMeasurement(@RequestBody @Valid BodyMeasurementRequest request) {
        BodyMeasurementResponse response = bodyMeasurementService.addMeasurement(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BodyMeasurementResponse>> getAllMeasurements() {
        List<BodyMeasurementResponse> response = bodyMeasurementService.getAllMeasurements();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/part/{part}")
    public ResponseEntity<List<BodyMeasurementResponse>> getAllMeasurementsForPart(@PathVariable String part) {
        List<BodyMeasurementResponse> response = bodyMeasurementService.getAllMeasurementsForPart(part);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<BodyMeasurementResponse>> getMeasurementsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BodyMeasurementResponse> response = bodyMeasurementService.getMeasurementsForDate(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/part/{part}/range/{from}/{to}")
    public ResponseEntity<List<BodyMeasurementResponse>> getAllMeasurementsForPartBetweenDates(
            @PathVariable String part,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<BodyMeasurementResponse> response = bodyMeasurementService
                .getAllMeasurementsForPartBetweenDates(part, from, to);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/progress/{part}")
    public ResponseEntity<ProgressResponse> getProgress(@PathVariable String part) {
        ProgressResponse response = bodyMeasurementService.getProgress(part);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/compare/{part}/{from}/{to}")
    public ResponseEntity<CompareResponse> compareInRange(
            @PathVariable String part,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        CompareResponse response = bodyMeasurementService.compareMeasurementsInRange(part, from, to);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable Long id) {
        bodyMeasurementService.deleteMeasurement(id);
        return ResponseEntity.noContent().build();
    }
}
