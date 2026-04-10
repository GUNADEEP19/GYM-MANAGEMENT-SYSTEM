package com.gym.dto;

import java.time.LocalDate;

@SuppressWarnings("unused")
public record MembershipValidityResponse(
        boolean valid,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        Long packageId,
        String packageName) {
}
