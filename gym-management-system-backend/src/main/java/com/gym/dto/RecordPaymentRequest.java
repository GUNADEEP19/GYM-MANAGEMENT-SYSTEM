package com.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@SuppressWarnings("unused")
public record RecordPaymentRequest(
        @SuppressWarnings("unused") @NotNull Long memberId,
        @SuppressWarnings("unused") @NotNull @Positive Double amount,
        @SuppressWarnings("unused") @NotBlank String method) {
}
