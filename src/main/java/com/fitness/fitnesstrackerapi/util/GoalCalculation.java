package com.fitness.fitnesstrackerapi.util;

import com.fitness.fitnesstrackerapi.model.entity.Goal;
import com.fitness.fitnesstrackerapi.model.entity.GoalProgressEntry;
import com.fitness.fitnesstrackerapi.model.entity.GoalStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GoalCalculation {

    public static void calculateGoalMetrics(Goal goal) {
        if (goal == null) return;

        LocalDate today = LocalDate.now();

        long totalDays = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate()) + 1;
        goal.setTotalDays(totalDays);

        long daysPassed = ChronoUnit.DAYS.between(goal.getStartDate(), today);
        daysPassed = Math.max(0, Math.min(daysPassed, totalDays));
        goal.setDaysPassed(daysPassed);

        double timePassedPercent = (totalDays == 0) ? 100.0 : ((double) daysPassed / totalDays) * 100;
        goal.setTimePassedPercent(round(timePassedPercent));

        double currentValue = calculateCurrentValue(goal.getProgressEntries(), goal.getStartValue());
        goal.setCurrentValue(currentValue);

        double progressPercent = calculateProgressPercent(goal.getStartValue(), goal.getTargetValue(), currentValue);
        goal.setProgressPercent(round(progressPercent));

        double timePenalty = Math.max(0, timePassedPercent - progressPercent);
        double goalSuccessPercent = Math.max(0, progressPercent - timePenalty);
        goal.setGoalSuccessPercent(round(goalSuccessPercent));

        goal.setStatus(determineStatus(goal, currentValue, today));
    }

    private static double calculateCurrentValue(List<GoalProgressEntry> entries, double defaultValue) {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        return entries.stream()
                .max(Comparator.comparing(GoalProgressEntry::getDate))
                .map(GoalProgressEntry::getValue)
                .orElse(defaultValue);
    }

    private static double calculateProgressPercent(double start, double target, double current) {
        double range = target - start;
        double progress = current - start;
        if (range == 0) return 100.0;
        return (progress / range) * 100.0;
    }

    private static GoalStatus determineStatus(Goal goal, double currentValue, LocalDate today) {
        if (currentValue == goal.getTargetValue()) {
            return GoalStatus.COMPLETED;
        } else if (today.isAfter(goal.getEndDate())) {
            return GoalStatus.FAILED;
        } else {
            return GoalStatus.IN_PROGRESS;
        }
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
