package com.gym.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@SuppressWarnings("unused")
public record CreatePackageRequest(
        @SuppressWarnings("unused") @NotBlank String name,
        @SuppressWarnings("unused") @NotNull @Min(1) Integer durationMonths,
        @SuppressWarnings("unused") @NotNull @Positive Double price) {
}
