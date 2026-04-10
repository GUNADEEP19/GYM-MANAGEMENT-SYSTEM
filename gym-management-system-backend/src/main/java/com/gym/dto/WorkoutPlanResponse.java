package com.gym.dto;

public record WorkoutPlanResponse(
        Long planId,
        String planName,
        String description,
        Integer durationWeeks,
        String difficultyLevel,
        long exerciseCount) {
}
