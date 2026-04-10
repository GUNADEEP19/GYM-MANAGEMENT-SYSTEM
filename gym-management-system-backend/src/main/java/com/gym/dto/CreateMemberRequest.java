package com.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@SuppressWarnings("unused")
public record CreateMemberRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String phone) {
}
