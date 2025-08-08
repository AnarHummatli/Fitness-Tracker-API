package com.fitness.fitnesstrackerapi.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "goal_progress_entries",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"goal_id", "date"})})
public class GoalProgressEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @NotNull
    @PastOrPresent
    @Column(nullable = false)
    private LocalDate date;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double value;
}
