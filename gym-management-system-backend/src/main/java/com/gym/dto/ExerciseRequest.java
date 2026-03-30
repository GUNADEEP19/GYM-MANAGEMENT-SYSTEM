package com.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRequest {
    @NotBlank(message = "Exercise name is required")
    private String exerciseName;

    @NotBlank(message = "Body part is required")
    private String bodyPart;

    private String equipmentRequired;

    @NotNull(message = "Sets is required")
    @Positive(message = "Sets must be positive")
    private Integer sets;

    @NotNull(message = "Reps is required")
    @Positive(message = "Reps must be positive")
    private Integer reps;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;

    private String instructions;

    private String videoUrl;

    @NotBlank(message = "Plan ID is required")
    private String planId;
}
