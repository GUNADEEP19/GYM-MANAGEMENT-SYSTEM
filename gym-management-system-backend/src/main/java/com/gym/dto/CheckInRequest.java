package com.gym.dto;

import java.time.LocalDate;

@SuppressWarnings("unused")
public record CheckInRequest(LocalDate attendanceDate) {
}
