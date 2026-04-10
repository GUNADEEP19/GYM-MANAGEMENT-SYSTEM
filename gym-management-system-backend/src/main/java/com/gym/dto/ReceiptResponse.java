package com.gym.dto;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public record ReceiptResponse(
        @SuppressWarnings("unused") String receiptNumber,
        @SuppressWarnings("unused") Long paymentId,
        @SuppressWarnings("unused") Long memberId,
        @SuppressWarnings("unused") String memberName,
        @SuppressWarnings("unused") Long packageId,
        @SuppressWarnings("unused") String packageName,
        @SuppressWarnings("unused") Double baseAmount,
        @SuppressWarnings("unused") Double discountAmount,
        @SuppressWarnings("unused") String discountCode,
        @SuppressWarnings("unused") Double amountPaid,
        @SuppressWarnings("unused") String paymentMethod,
        @SuppressWarnings("unused") String paymentStatus,
        @SuppressWarnings("unused") LocalDateTime paidAt) {
}
