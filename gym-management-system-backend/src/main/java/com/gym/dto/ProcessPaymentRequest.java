package com.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@SuppressWarnings("unused")
public record ProcessPaymentRequest(
        @SuppressWarnings("unused") @NotNull Long packageId,
        @SuppressWarnings("unused") @NotNull @Positive Double amount,
        @SuppressWarnings("unused") @NotBlank String paymentMethod,
        @SuppressWarnings("unused") String discountCode) {
}
