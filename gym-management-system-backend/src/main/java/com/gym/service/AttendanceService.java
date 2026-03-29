package com.gym.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.AttendanceRequest;
import com.gym.dto.AttendanceResponse;
import com.gym.model.Attendance;
import com.gym.model.Member;
import com.gym.repository.AttendanceRepository;
import com.gym.repository.MemberRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, MemberRepository memberRepository) {
        this.attendanceRepository = attendanceRepository;
        this.memberRepository = memberRepository;
    }

    public AttendanceResponse markCheckIn(AttendanceRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setAttendanceDate(request.getAttendanceDate() != null ? request.getAttendanceDate() : LocalDate.now());
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setStatus("CHECKED_IN");

        Attendance saved = attendanceRepository.save(attendance);
        return toAttendanceResponse(saved);
    }

    public AttendanceResponse markCheckOut(String attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found"));

        if (!attendance.getStatus().equals("CHECKED_IN")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only check out from checked-in status");
        }

        attendance.setCheckOutTime(LocalDateTime.now());
        attendance.setStatus("CHECKED_OUT");

        Attendance updated = attendanceRepository.save(attendance);
        return toAttendanceResponse(updated);
    }

    public List<AttendanceResponse> getAttendanceByMember(String memberId) {
        List<Attendance> records = attendanceRepository.findByMemberId(memberId);
        return records.stream().map(this::toAttendanceResponse).collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAttendanceByDateRange(String memberId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> records = attendanceRepository.findByMemberIdAndAttendanceDateBetween(memberId, startDate,
                endDate);
        return records.stream().map(this::toAttendanceResponse).collect(Collectors.toList());
    }

    public Integer getAttendanceCount(String memberId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.countAttendanceInDateRange(memberId, startDate, endDate);
    }

    private AttendanceResponse toAttendanceResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .attendanceId(attendance.getAttendanceId())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .createdAt(attendance.getCreatedAt())
                .build();
    }
}
