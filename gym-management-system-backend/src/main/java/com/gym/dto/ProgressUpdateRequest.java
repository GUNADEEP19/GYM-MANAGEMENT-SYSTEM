package com.gym.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@SuppressWarnings("unused")
public record ProgressUpdateRequest(
        @SuppressWarnings("unused") @NotNull Long planId,
        @SuppressWarnings("unused") @NotNull @Min(1) Integer weekNumber,
        @SuppressWarnings("unused") @NotNull @Min(0) Integer exercisesDone,
        @SuppressWarnings("unused") @NotNull @Positive Double weight,
        @SuppressWarnings("unused") @NotNull @Positive Double bmi,
        @SuppressWarnings("unused") String progressNotes) {
}
