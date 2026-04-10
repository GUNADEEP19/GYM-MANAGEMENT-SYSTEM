package com.gym.dto;

import jakarta.validation.constraints.NotBlank;

@SuppressWarnings("unused")
public record UpdateMemberStatusRequest(@NotBlank String status) {
}
