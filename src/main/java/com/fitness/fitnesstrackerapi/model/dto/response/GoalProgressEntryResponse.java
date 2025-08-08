package com.fitness.fitnesstrackerapi.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalProgressEntryResponse {
    private Long id;
    private LocalDate date;
    private Double value;
}
