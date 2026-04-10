package com.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@SuppressWarnings("unused")
public record ProcessPaymentRequest(
        @NotNull Long packageId,
        @NotNull @Positive Double amount,
        @NotBlank String paymentMethod,
        String discountCode) {
}
