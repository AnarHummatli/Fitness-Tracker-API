package com.fitness.fitnesstrackerapi.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_step_goals",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "start_date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyStepGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Min(1)
    @Column(name = "daily_step_goal", nullable = false)
    private Integer dailyStepGoal;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Min(1)
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Transient
    public LocalDate getEndDate() {
        return startDate.plusDays(durationDays - 1);
    }
}
