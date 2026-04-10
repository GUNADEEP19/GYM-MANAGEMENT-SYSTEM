package com.gym.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateExerciseRequest(
        @NotBlank String exerciseName,
        @NotNull @Min(1) Integer sets,
        @NotNull @Min(1) Integer reps,
        @NotBlank String bodyPart,
        String instructions) {
}
