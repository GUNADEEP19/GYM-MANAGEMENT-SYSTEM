package com.gym.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.CheckInRequest;
import com.gym.model.Attendance;
import com.gym.model.AttendanceStatus;
import com.gym.model.Member;
import com.gym.repository.AttendanceRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberService memberService;
    private final MembershipService membershipService;

    public AttendanceService(AttendanceRepository attendanceRepository, MemberService memberService, MembershipService membershipService) {
        this.attendanceRepository = attendanceRepository;
        this.memberService = memberService;
        this.membershipService = membershipService;
    }

    public Attendance checkIn(Long memberId, CheckInRequest request) {
        Member member = memberService.getById(memberId);
        LocalDate date = request != null && request.attendanceDate() != null ? request.attendanceDate() : LocalDate.now();

        // Use-case: Mark Attendance <<include>> Check Membership Validity
        membershipService.requireActiveForMemberOn(memberId, date);

        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setAttendanceDate(date);
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setStatus(AttendanceStatus.CHECKED_IN);
        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(Long memberId, Long attendanceId) {
        Objects.requireNonNull(attendanceId, "attendanceId");
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not found"));
        if (!attendance.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot checkout another member");
        }
        if (attendance.getStatus() != AttendanceStatus.CHECKED_IN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attendance already checked out");
        }
        attendance.setCheckOutTime(LocalDateTime.now());
        attendance.setStatus(AttendanceStatus.CHECKED_OUT);
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> listAll() {
        return attendanceRepository.findAll();
    }

    public List<Attendance> listByMember(Long memberId) {
        return attendanceRepository.findByMemberId(memberId);
    }
}
