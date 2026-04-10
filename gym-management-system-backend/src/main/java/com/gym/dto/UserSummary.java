package com.gym.dto;

@SuppressWarnings("unused")
public record UserSummary(
        Long userId,
        String name,
        String email,
        String phone,
        String role) {
}
