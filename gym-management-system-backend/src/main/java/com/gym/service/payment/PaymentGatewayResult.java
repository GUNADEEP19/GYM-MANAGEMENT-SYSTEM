package com.gym.service.payment;

public record PaymentGatewayResult(boolean approved, String reason) {

    public boolean declined() {
        return !approved;
    }

    public String reasonOrEmpty() {
        return reason == null ? "" : reason;
    }

    public static PaymentGatewayResult approvedResult() {
        return new PaymentGatewayResult(true, null);
    }

    public static PaymentGatewayResult declined(String reason) {
        return new PaymentGatewayResult(false, reason);
    }
}
