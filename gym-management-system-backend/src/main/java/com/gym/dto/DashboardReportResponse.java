package com.gym.dto;

public record DashboardReportResponse(
        long totalMembers,
        long activeTrainers,
        double totalRevenue,
        long todayAttendanceCount) {
}
