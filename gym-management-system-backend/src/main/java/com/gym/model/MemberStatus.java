package com.gym.model;

public enum MemberStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED;

    public static MemberStatus fromValue(String value) {
        return switch (value.trim().toUpperCase()) {
            case "ACTIVE" -> ACTIVE;
            case "INACTIVE" -> INACTIVE;
            case "SUSPENDED" -> SUSPENDED;
            default -> throw new IllegalArgumentException("Invalid member status: " + value);
        };
    }
}
