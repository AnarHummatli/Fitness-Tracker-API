package com.fitness.fitnesstrackerapi.model.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class GoalRequest {

    @NotBlank
    private String type;

    @NotNull
    @Positive
    private Double startValue;

    @NotNull
    @Positive
    private Double targetValue;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
