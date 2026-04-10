package com.gym.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public record AttendanceResponse(
        Long attendanceId,
        LocalDate attendanceDate,
        String status,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime) {
}
