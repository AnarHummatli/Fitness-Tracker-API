package com.fitness.fitnesstrackerapi.model.dto.response;

import com.fitness.fitnesstrackerapi.model.entity.WorkoutSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class WorkoutSessionResponse {
    private Long sessionId;
    private LocalDate date;
    private WorkoutSessionStatus workoutSessionStatus;
    private List<WorkoutEntryResponse> entries;
}
