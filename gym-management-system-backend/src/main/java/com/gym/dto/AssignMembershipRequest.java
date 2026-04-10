package com.gym.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

@SuppressWarnings("unused")
public record AssignMembershipRequest(
        @SuppressWarnings("unused") @NotNull Long memberId,
        @SuppressWarnings("unused") @NotNull Long packageId,
        @SuppressWarnings("unused") LocalDate startDate) {
}
