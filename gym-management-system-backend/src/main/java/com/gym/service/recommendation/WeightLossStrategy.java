package com.gym.service.recommendation;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class WeightLossStrategy implements WorkoutRecommendationStrategy {
    @Override
    public String name() {
        return "WEIGHT_LOSS";
    }

    @Override
    public String advisedGoal() {
        return "Reduce body fat and improve cardiovascular endurance";
    }

    @Override
    public int recommendedDurationWeeks() {
        return 8;
    }

    @Override
    public List<String> recommendedExercises() {
        return List.of("Brisk Walking", "Cycling", "Jump Rope", "Burpees", "Mountain Climbers");
    }
}
