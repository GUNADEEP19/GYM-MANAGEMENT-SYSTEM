package com.gym.controller;

import java.time.LocalDate;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.DashboardReportResponse;
import com.gym.model.UserRole;
import com.gym.repository.AppUserRepository;
import com.gym.repository.AttendanceRepository;
import com.gym.repository.MemberRepository;
import com.gym.repository.PaymentRepository;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final MemberRepository memberRepository;
    private final AppUserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final AttendanceRepository attendanceRepository;

    public ReportController(MemberRepository memberRepository, AppUserRepository userRepository,
            PaymentRepository paymentRepository, AttendanceRepository attendanceRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DashboardReportResponse> dashboard() {
        long totalMembers = memberRepository.count();
        long trainers = userRepository.countByRole(UserRole.TRAINER);
        double revenue = paymentRepository.sumSuccessfulRevenue();
        long todayAttendance = attendanceRepository.countByAttendanceDate(LocalDate.now());
        return ApiResponse.ok(new DashboardReportResponse(totalMembers, trainers, revenue, todayAttendance));
    }
}
