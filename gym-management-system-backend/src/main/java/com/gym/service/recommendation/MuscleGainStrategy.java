package com.gym.service.recommendation;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MuscleGainStrategy implements WorkoutRecommendationStrategy {
    @Override
    public String name() {
        return "MUSCLE_GAIN";
    }

    @Override
    public String advisedGoal() {
        return "Increase muscle mass with progressive overload";
    }

    @Override
    public int recommendedDurationWeeks() {
        return 12;
    }

    @Override
    public List<String> recommendedExercises() {
        return List.of("Squats", "Deadlifts", "Bench Press", "Pull-ups", "Overhead Press");
    }
}
