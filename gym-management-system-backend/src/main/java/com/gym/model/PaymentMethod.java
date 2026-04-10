package com.gym.model;

public enum PaymentMethod {
    CASH,
    CARD,
    UPI;

    public static PaymentMethod fromValue(String value) {
        return switch (value.trim().toUpperCase()) {
            case "CASH" -> CASH;
            case "CARD", "CREDIT_CARD", "CREDITCARD" -> CARD;
            case "UPI" -> UPI;
            default -> throw new IllegalArgumentException("Invalid payment method: " + value);
        };
    }
}
