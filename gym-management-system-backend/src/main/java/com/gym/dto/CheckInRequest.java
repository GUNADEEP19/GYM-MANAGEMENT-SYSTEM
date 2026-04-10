package com.gym.dto;

import java.time.LocalDate;

public record CheckInRequest(LocalDate attendanceDate) {
}
