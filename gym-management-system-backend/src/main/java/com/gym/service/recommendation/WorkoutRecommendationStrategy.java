package com.gym.service.recommendation;

import java.util.List;

public interface WorkoutRecommendationStrategy {
    String name();
    String advisedGoal();
    int recommendedDurationWeeks();
    List<String> recommendedExercises();
}
