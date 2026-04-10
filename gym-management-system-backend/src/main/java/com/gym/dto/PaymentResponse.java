package com.gym.dto;

import java.time.LocalDateTime;

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
