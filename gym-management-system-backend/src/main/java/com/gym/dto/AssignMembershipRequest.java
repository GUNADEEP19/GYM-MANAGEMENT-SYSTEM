package com.gym.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

@SuppressWarnings("unused")
public record AssignMembershipRequest(
        @NotNull Long memberId,
        @NotNull Long packageId,
        LocalDate startDate) {
}
