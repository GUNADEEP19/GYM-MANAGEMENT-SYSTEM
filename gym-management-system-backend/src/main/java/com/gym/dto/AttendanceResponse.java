package com.gym.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {
    private String attendanceId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDate attendanceDate;
    private String status;
    private LocalDateTime createdAt;
}
