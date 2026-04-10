package com.gym.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@SuppressWarnings("unused")
public record CreateExerciseRequest(
        @SuppressWarnings("unused") @NotBlank String exerciseName,
        @SuppressWarnings("unused") @NotNull @Min(1) Integer sets,
        @SuppressWarnings("unused") @NotNull @Min(1) Integer reps,
        @SuppressWarnings("unused") @NotBlank String bodyPart,
        @SuppressWarnings("unused") String instructions) {
}
