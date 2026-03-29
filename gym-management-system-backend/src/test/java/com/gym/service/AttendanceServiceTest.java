package com.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.AttendanceRequest;
import com.gym.model.Attendance;
import com.gym.model.Member;
import com.gym.repository.AttendanceRepository;
import com.gym.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @Test
    void markCheckInThrowsWhenMemberNotFound() {
        AttendanceRequest request = new AttendanceRequest();
        request.setMemberId("invalid-member");

        when(memberRepository.findById("invalid-member")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> attendanceService.markCheckIn(request));
    }

    @Test
    void markCheckInSuccessfully() {
        AttendanceRequest request = new AttendanceRequest();
        request.setMemberId("member-1");
        request.setAttendanceDate(LocalDate.now());

        Member member = new Member();
        member.setUserId("member-1");

        Attendance savedAttendance = new Attendance();
        savedAttendance.setAttendanceId("att-1");
        savedAttendance.setStatus("CHECKED_IN");
        savedAttendance.setAttendanceDate(LocalDate.now());

        when(memberRepository.findById("member-1")).thenReturn(Optional.of(member));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(savedAttendance);

        var response = attendanceService.markCheckIn(request);

        assertNotNull(response);
        assertEquals("att-1", response.getAttendanceId());
        assertEquals("CHECKED_IN", response.getStatus());
        verify(attendanceRepository).save(any(Attendance.class));
    }

    @Test
    void markCheckOutThrowsWhenAttendanceNotFound() {
        when(attendanceRepository.findById("invalid-att")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> attendanceService.markCheckOut("invalid-att"));
    }

    @Test
    void markCheckOutSuccessfully() {
        Attendance existingAttendance = new Attendance();
        existingAttendance.setAttendanceId("att-1");
        existingAttendance.setStatus("CHECKED_IN");

        Attendance updatedAttendance = new Attendance();
        updatedAttendance.setAttendanceId("att-1");
        updatedAttendance.setStatus("CHECKED_OUT");

        when(attendanceRepository.findById("att-1")).thenReturn(Optional.of(existingAttendance));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(updatedAttendance);

        var response = attendanceService.markCheckOut("att-1");

        assertNotNull(response);
        assertEquals("CHECKED_OUT", response.getStatus());
        verify(attendanceRepository).save(any(Attendance.class));
    }
}
