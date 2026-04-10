package com.gym.dto;

public record GymPackageResponse(
        Long packageId,
        String packageName,
        Integer durationMonths,
        Double price,
        String benefits) {
}
