package com.fitness.fitnesstrackerapi.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "goals", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "type"})})
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String type;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double startValue;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double targetValue;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status = GoalStatus.IN_PROGRESS;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalProgressEntry> progressEntries = new ArrayList<>();

    @Transient
    private Double currentValue;

    @Transient
    private Double progressPercent;

    @Transient
    private Double timePassedPercent;

    @Transient
    private Double goalSuccessPercent;

    @Transient
    private Long daysPassed;

    @Transient
    private Long totalDays;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = GoalStatus.IN_PROGRESS;
        }
    }
}