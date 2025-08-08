package com.fitness.fitnesstrackerapi.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalProgressEntryRequest {

    @NotNull
    private Long goalId;

    @NotNull
    private LocalDate date;

    @NotNull
    @Positive
    private Double value;
}
