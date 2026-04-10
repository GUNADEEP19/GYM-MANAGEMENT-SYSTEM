package com.gym.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateMemberStatusRequest(@NotBlank String status) {
}
