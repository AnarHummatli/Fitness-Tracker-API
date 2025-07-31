package com.fitness.fitnesstrackerapi.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressResponse {

    private String part;
    private Double startValue;
    private Double currentValue;
    private Double difference;
    private Double percentageChange;
    private Long daysTracked;
    private Double averageChangePerDay;
}
