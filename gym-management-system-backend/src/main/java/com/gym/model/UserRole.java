package com.gym.model;

public enum UserRole {
    ADMIN,
    TRAINER,
    MEMBER;

    public static UserRole fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }
        return UserRole.valueOf(value.trim().toUpperCase());
    }
}
