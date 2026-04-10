package com.gym.service.recommendation;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GeneralFitnessStrategy implements WorkoutRecommendationStrategy {
    @Override
    public String name() {
        return "GENERAL_FITNESS";
    }

    @Override
    public String advisedGoal() {
        return "Maintain a balanced routine for overall health";
    }

    @Override
    public int recommendedDurationWeeks() {
        return 6;
    }

    @Override
    public List<String> recommendedExercises() {
        return List.of("Push-ups", "Lunges", "Plank", "Rowing", "Light Jogging");
    }
}
