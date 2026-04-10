package com.gym.dto;

@SuppressWarnings("unused")
public record AuthResponse(
        @SuppressWarnings("unused") Long userId,
        @SuppressWarnings("unused") String name,
        @SuppressWarnings("unused") String email,
        @SuppressWarnings("unused") String phone,
        @SuppressWarnings("unused") String role,
        @SuppressWarnings("unused") String token) {
}
