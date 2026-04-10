package com.gym.dto;

@SuppressWarnings("unused")
public record ProgressRecordResponse(
        @SuppressWarnings("unused") Long progressId,
        @SuppressWarnings("unused") Integer weekNumber,
        @SuppressWarnings("unused") Integer exercisesDone,
        @SuppressWarnings("unused") Double weight,
        @SuppressWarnings("unused") Double bmi,
        @SuppressWarnings("unused") String progressNotes,
        @SuppressWarnings("unused") Long planId,
        @SuppressWarnings("unused") String planName) {
}
