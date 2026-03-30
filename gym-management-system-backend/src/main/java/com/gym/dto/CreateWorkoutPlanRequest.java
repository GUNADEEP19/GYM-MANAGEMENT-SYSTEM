package com.gym.dto;

import java.time.LocalDateTime;
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
public class CreateWorkoutPlanRequest {
    @NotBlank(message = "Plan name is required")
    private String planName;

    private String description;

    @NotBlank(message = "Fitness goal is required")
    private String fitnessGoal;

    @NotBlank(message = "Difficulty level is required")
    private String difficultyLevel;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Duration weeks is required")
    @Positive(message = "Duration weeks must be positive")
    private Integer durationWeeks;

    @NotBlank(message = "Member ID is required")
    private String memberId;

    @NotBlank(message = "Trainer ID is required")
    private String trainerId;
}
