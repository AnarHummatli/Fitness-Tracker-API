package com.fitness.fitnesstrackerapi.model.dto.response;

import com.fitness.fitnesstrackerapi.model.entity.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalResponse {

    private Long id;
    private GoalStatus status;

    private String type;
    private Double startValue;
    private Double targetValue;
    private Double currentValue;
    private Double progressPercent;
    private Double goalSuccessPercent;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long daysPassed;
    private Long totalDays;
    private Double timePassedPercent;

}
