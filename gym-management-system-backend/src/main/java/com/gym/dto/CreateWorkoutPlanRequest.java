package com.gym.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@SuppressWarnings("unused")
public record CreateWorkoutPlanRequest(
        @SuppressWarnings("unused") @NotNull Long memberId,
        @SuppressWarnings("unused") @NotBlank String planName,
        @SuppressWarnings("unused") String description,
        @SuppressWarnings("unused") @NotNull @Min(1) Integer durationWeeks,
        @SuppressWarnings("unused") @NotBlank String difficultyLevel,
        @SuppressWarnings("unused") List<CreateExerciseRequest> exercises) {
}
