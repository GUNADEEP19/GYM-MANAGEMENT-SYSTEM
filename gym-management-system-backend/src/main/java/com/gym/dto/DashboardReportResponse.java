package com.gym.dto;

@SuppressWarnings("unused")
public record DashboardReportResponse(
        long totalMembers,
        long activeTrainers,
        double totalRevenue,
        long todayAttendanceCount) {
}
