package com.gym.dto;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public record PaymentResponse(
        @SuppressWarnings("unused") Long paymentId,
        @SuppressWarnings("unused") Double amount,
        @SuppressWarnings("unused") LocalDateTime paidAt,
        @SuppressWarnings("unused") String method,
        @SuppressWarnings("unused") String status,
        @SuppressWarnings("unused") String receiptNumber,
        @SuppressWarnings("unused") Long packageId,
        @SuppressWarnings("unused") String packageName,
        @SuppressWarnings("unused") String discountCode,
        @SuppressWarnings("unused") Double baseAmount,
        @SuppressWarnings("unused") Double discountAmount) {
}
