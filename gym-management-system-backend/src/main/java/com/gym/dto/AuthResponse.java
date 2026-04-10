package com.gym.dto;

public record AuthResponse(
        Long userId,
        String name,
        String email,
        String phone,
        String role,
        String token) {
}
