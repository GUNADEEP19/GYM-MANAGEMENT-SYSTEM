package com.gym.dto;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public record PaymentResponse(
        Long paymentId,
        Double amount,
        LocalDateTime paidAt,
        String method,
        String status,
        String receiptNumber,
        Long packageId,
        String packageName,
        String discountCode,
        Double baseAmount,
        Double discountAmount) {
}
