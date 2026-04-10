package com.gym.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePackageRequest(
        @NotBlank String name,
        @NotNull @Min(1) Integer durationMonths,
        @NotNull @Positive Double price) {
}
