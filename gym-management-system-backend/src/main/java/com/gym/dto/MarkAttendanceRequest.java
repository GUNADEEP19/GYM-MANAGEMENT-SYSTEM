package com.gym.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record MarkAttendanceRequest(
        @NotNull Long memberId,
        LocalDateTime visitTime) {
}
