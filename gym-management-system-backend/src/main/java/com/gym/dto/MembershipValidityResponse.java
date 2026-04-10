package com.gym.dto;

import java.time.LocalDate;

@SuppressWarnings("unused")
public record MembershipValidityResponse(
        @SuppressWarnings("unused") boolean valid,
        @SuppressWarnings("unused") String status,
        @SuppressWarnings("unused") LocalDate startDate,
        @SuppressWarnings("unused") LocalDate endDate,
        @SuppressWarnings("unused") Long packageId,
        @SuppressWarnings("unused") String packageName) {
}
