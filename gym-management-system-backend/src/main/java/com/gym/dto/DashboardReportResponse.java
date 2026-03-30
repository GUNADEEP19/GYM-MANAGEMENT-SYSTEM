package com.gym.dto;

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
public class DashboardReportResponse {
    private long totalMembers;
    private long activeTrainers;
    private double totalRevenue;
    private long todayAttendanceCount;
}
