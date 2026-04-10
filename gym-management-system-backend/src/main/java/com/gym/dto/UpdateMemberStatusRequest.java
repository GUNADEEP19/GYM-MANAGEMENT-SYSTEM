package com.gym.dto;

import jakarta.validation.constraints.NotBlank;

@SuppressWarnings("unused")
public record UpdateMemberStatusRequest(@SuppressWarnings("unused") @NotBlank String status) {
}
