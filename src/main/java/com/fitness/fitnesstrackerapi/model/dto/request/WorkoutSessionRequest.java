package com.fitness.fitnesstrackerapi.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WorkoutSessionRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    @Valid
    private List<WorkoutEntryRequest> entries;
}
