package com.gym.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public record AttendanceResponse(
        @SuppressWarnings("unused") Long attendanceId,
        @SuppressWarnings("unused") LocalDate attendanceDate,
        @SuppressWarnings("unused") String status,
        @SuppressWarnings("unused") LocalDateTime checkInTime,
        @SuppressWarnings("unused") LocalDateTime checkOutTime) {
}
