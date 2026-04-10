package com.gym.dto;

@SuppressWarnings("unused")
public record ExerciseResponse(
        @SuppressWarnings("unused") Long exerciseId,
        @SuppressWarnings("unused") String exerciseName,
        @SuppressWarnings("unused") Integer sets,
        @SuppressWarnings("unused") Integer reps,
        @SuppressWarnings("unused") String bodyPart,
        @SuppressWarnings("unused") String instructions) {
}
