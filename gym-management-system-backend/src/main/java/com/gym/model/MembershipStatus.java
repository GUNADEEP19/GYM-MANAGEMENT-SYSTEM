package com.gym.model;

public enum MembershipStatus {
    ACTIVE,
    EXPIRED,
    CANCELLED;

    public static MembershipStatus fromValue(String value) {
        return switch (value.trim().toUpperCase()) {
            case "ACTIVE" -> ACTIVE;
            case "EXPIRED" -> EXPIRED;
            case "CANCELLED" -> CANCELLED;
            default -> throw new IllegalArgumentException("Invalid membership status: " + value);
        };
    }
}
