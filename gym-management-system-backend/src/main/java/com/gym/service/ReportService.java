package com.gym.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.gym.dto.DashboardReportResponse;
import com.gym.model.Payment;
import com.gym.repository.AttendanceRepository;
import com.gym.repository.MemberRepository;
import com.gym.repository.PaymentRepository;
import com.gym.repository.TrainerRepository;

@Service
public class ReportService {

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final PaymentRepository paymentRepository;
    private final AttendanceRepository attendanceRepository;

    public ReportService(MemberRepository memberRepository,
                         TrainerRepository trainerRepository,
                         PaymentRepository paymentRepository,
                         AttendanceRepository attendanceRepository) {
        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
        this.paymentRepository = paymentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public DashboardReportResponse generateDashboardReport() {
        long totalMembers = memberRepository.count();
        long activeTrainers = trainerRepository.count();
        
        double totalRevenue = paymentRepository.findSuccessfulPayments().stream()
                                .mapToDouble(Payment::getAmount)
                                .sum();
                                
        long todayAttendance = attendanceRepository.countByAttendanceDate(LocalDate.now());

        return DashboardReportResponse.builder()
                .totalMembers(totalMembers)
                .activeTrainers(activeTrainers)
                .totalRevenue(totalRevenue)
                .todayAttendanceCount(todayAttendance)
                .build();
    }
}
