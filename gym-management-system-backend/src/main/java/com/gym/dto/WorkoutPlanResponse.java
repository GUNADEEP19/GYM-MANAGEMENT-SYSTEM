package com.gym.dto;

@SuppressWarnings("unused")
public record WorkoutPlanResponse(
        Long planId,
        String planName,
        String description,
        Integer durationWeeks,
        String difficultyLevel,
        long exerciseCount) {
}
