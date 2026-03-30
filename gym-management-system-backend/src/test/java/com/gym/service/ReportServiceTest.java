package com.gym.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gym.dto.DashboardReportResponse;
import com.gym.model.Payment;
import com.gym.repository.AttendanceRepository;
import com.gym.repository.MemberRepository;
import com.gym.repository.PaymentRepository;
import com.gym.repository.TrainerRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Report Service Tests")
class ReportServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("Should aggregate statistics correctly from repositories")
    void testGenerateDashboardReport() {
        // Arrange
        Payment p1 = new Payment();
        p1.setAmount(100.0);
        Payment p2 = new Payment();
        p2.setAmount(50.0);

        when(memberRepository.count()).thenReturn(150L);
        when(trainerRepository.count()).thenReturn(12L);
        when(paymentRepository.findSuccessfulPayments()).thenReturn(Arrays.asList(p1, p2));
        when(attendanceRepository.countByAttendanceDate(any())).thenReturn(45L);

        // Act
        DashboardReportResponse response = reportService.generateDashboardReport();

        // Assert
        assertNotNull(response);
        assertEquals(150L, response.getTotalMembers());
        assertEquals(12L, response.getActiveTrainers());
        assertEquals(150.0, response.getTotalRevenue());
        assertEquals(45L, response.getTodayAttendanceCount());

        verify(memberRepository, times(1)).count();
        verify(trainerRepository, times(1)).count();
        verify(paymentRepository, times(1)).findSuccessfulPayments();
        verify(attendanceRepository, times(1)).countByAttendanceDate(any());
    }
}
