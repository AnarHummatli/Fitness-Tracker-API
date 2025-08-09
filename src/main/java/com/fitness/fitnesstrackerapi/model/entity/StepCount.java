package com.fitness.fitnesstrackerapi.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "step_counts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer stepCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StepGoalStatus status = StepGoalStatus.NO_GOAL;

    @Transient
    public Double getCaloriesBurned() {
        return stepCount != null ? stepCount * 0.04 : 0.0;
    }
}

