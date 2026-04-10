package com.gym.service.payment;

public record PaymentGatewayRequest(
        double requiredAmount,
        Double providedAmount,
        String method,
        String discountCode) {

        public PaymentGatewayRequest {
                // Normalize values defensively so downstream validation is consistent.
                if (Double.isNaN(requiredAmount) || Double.isInfinite(requiredAmount)) {
                        requiredAmount = 0.0;
                }

                if (providedAmount != null && (providedAmount.isNaN() || providedAmount.isInfinite())) {
                        providedAmount = null;
                }

                method = method == null ? null : method.trim();
                discountCode = discountCode == null ? null : discountCode.trim();
        }
}
