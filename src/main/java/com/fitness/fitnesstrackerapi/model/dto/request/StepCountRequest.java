package com.fitness.fitnesstrackerapi.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StepCountRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    @PositiveOrZero
    private Integer stepCount;
}
