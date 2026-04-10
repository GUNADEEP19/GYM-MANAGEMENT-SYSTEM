package com.gym.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@SuppressWarnings("unused")
public record CreateWorkoutPlanRequest(
        @NotNull Long memberId,
        @NotBlank String planName,
        String description,
        @NotNull @Min(1) Integer durationWeeks,
        @NotBlank String difficultyLevel,
        List<CreateExerciseRequest> exercises) {
}
