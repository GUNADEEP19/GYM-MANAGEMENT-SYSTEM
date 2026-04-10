package com.gym.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProgressUpdateRequest(
        @NotNull Long planId,
        @NotNull @Min(1) Integer weekNumber,
        @NotNull @Min(0) Integer exercisesDone,
        @NotNull @Positive Double weight,
        @NotNull @Positive Double bmi,
        String progressNotes) {
}
