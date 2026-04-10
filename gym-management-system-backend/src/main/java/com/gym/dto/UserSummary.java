package com.gym.dto;

public record UserSummary(
        Long userId,
        String name,
        String email,
        String phone,
        String role) {
}
