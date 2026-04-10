package com.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@SuppressWarnings("unused")
public record CreateMemberRequest(
        @SuppressWarnings("unused") @NotBlank String name,
        @SuppressWarnings("unused") @Email @NotBlank String email,
        @SuppressWarnings("unused") @NotBlank String phone) {
}
