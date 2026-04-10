package com.gym.dto;

import java.util.List;

public record RecommendationResponse(
        String memberName,
        Double currentBmi,
        String selectedStrategy,
        String advisedGoal,
        Integer recommendedDurationWeeks,
        List<String> recommendedExercises) {
}
