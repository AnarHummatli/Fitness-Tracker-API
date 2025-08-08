package com.fitness.fitnesstrackerapi.model.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompareResponse {

    private String part;
    private Double startValue;
    private Double endValue;
    private Double difference;
    private Double percentageChange;
    private Long daysTracked;
    private Double averageChangePerDay;
}
