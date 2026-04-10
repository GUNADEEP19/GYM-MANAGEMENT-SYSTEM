package com.gym.dto;

import java.util.List;

@SuppressWarnings("unused")
public record RecommendationResponse(
        @SuppressWarnings("unused") String memberName,
        @SuppressWarnings("unused") Double currentBmi,
        @SuppressWarnings("unused") String selectedStrategy,
        @SuppressWarnings("unused") String advisedGoal,
        @SuppressWarnings("unused") Integer recommendedDurationWeeks,
        @SuppressWarnings("unused") List<String> recommendedExercises) {
}
