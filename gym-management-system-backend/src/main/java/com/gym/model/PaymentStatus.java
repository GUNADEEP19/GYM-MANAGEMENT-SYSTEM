package com.gym.model;

public enum PaymentStatus {
    SUCCESS,
    FAILED,
    PENDING;

    public static PaymentStatus fromValue(String value) {
        return switch (value.trim().toUpperCase()) {
            case "SUCCESS" -> SUCCESS;
            case "FAILED" -> FAILED;
            case "PENDING" -> PENDING;
            default -> throw new IllegalArgumentException("Invalid payment status: " + value);
        };
    }
}
