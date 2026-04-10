package com.gym.dto;

public record ProgressRecordResponse(
        Long progressId,
        Integer weekNumber,
        Integer exercisesDone,
        Double weight,
        Double bmi,
        String progressNotes,
        Long planId,
        String planName) {
}
