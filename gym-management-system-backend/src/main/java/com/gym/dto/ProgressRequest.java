package com.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
public class ProgressRequest {
    @NotNull(message = "Week number is required")
    @Positive(message = "Week number must be positive")
    private Integer weekNumber;

    @NotNull(message = "Exercises done is required")
    @PositiveOrZero(message = "Exercises done cannot be negative")
    private Integer exercisesDone;

    private String progressNotes;

    @NotBlank(message = "Member ID is required")
    private String memberId;

    @NotBlank(message = "Plan ID is required")
    private String planId;
}
