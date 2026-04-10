package com.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@SuppressWarnings("unused")
public record RegisterRequest(
        @SuppressWarnings("unused") @NotBlank String name,
        @SuppressWarnings("unused") @Email @NotBlank String email,
        @SuppressWarnings("unused") @NotBlank String phone,
        @SuppressWarnings("unused") @NotBlank @Size(min = 8, max = 72, message = "Password must be 8-72 characters") String password,
        @SuppressWarnings("unused") String userType,
        @SuppressWarnings("unused") Long trainerUserId) {
}
