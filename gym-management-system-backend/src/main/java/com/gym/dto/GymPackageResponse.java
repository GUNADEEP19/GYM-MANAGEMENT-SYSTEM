package com.gym.dto;

@SuppressWarnings("unused")
public record GymPackageResponse(
        Long packageId,
        String packageName,
        Integer durationMonths,
        Double price,
        String benefits) {
}
