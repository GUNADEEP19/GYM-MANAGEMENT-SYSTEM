package com.gym.dto;

@SuppressWarnings("unused")
public record DashboardReportResponse(
        @SuppressWarnings("unused") long totalMembers,
        @SuppressWarnings("unused") long activeTrainers,
        @SuppressWarnings("unused") double totalRevenue,
        @SuppressWarnings("unused") long todayAttendanceCount) {
}
