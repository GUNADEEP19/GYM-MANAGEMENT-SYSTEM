package com.gym.dto;

@SuppressWarnings("unused")
public record WorkoutPlanResponse(
        @SuppressWarnings("unused") Long planId,
        @SuppressWarnings("unused") String planName,
        @SuppressWarnings("unused") String description,
        @SuppressWarnings("unused") Integer durationWeeks,
        @SuppressWarnings("unused") String difficultyLevel,
        @SuppressWarnings("unused") long exerciseCount) {
}
