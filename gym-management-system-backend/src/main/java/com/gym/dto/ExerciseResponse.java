package com.gym.dto;

public record ExerciseResponse(
        Long exerciseId,
        String exerciseName,
        Integer sets,
        Integer reps,
        String bodyPart,
        String instructions) {
}
