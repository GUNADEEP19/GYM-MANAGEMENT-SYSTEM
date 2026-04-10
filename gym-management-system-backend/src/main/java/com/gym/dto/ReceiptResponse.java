package com.gym.dto;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public record ReceiptResponse(
        String receiptNumber,
        Long paymentId,
        Long memberId,
        String memberName,
        Long packageId,
        String packageName,
        Double baseAmount,
        Double discountAmount,
        String discountCode,
        Double amountPaid,
        String paymentMethod,
        String paymentStatus,
        LocalDateTime paidAt) {
}
