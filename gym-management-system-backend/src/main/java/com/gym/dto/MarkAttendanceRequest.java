package com.gym.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

@SuppressWarnings("unused")
public record MarkAttendanceRequest(
        @SuppressWarnings("unused") @NotNull Long memberId,
        @SuppressWarnings("unused") LocalDateTime visitTime) {
}
