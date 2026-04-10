package com.gym.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.AttendanceResponse;
import com.gym.dto.CheckInRequest;
import com.gym.model.Attendance;
import com.gym.security.CurrentUser;
import com.gym.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
@Validated
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final CurrentUser currentUser;

    public AttendanceController(AttendanceService attendanceService, CurrentUser currentUser) {
        this.attendanceService = attendanceService;
        this.currentUser = currentUser;
    }

    @PostMapping("/checkin")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<AttendanceResponse> checkIn(@RequestBody CheckInRequest request) {
        Long memberId = currentUser.requireMemberId();
        Attendance attendance = attendanceService.checkIn(memberId, request);
        return ApiResponse.ok(toResponse(attendance));
    }

    @PostMapping("/checkout/{attendanceId}")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<AttendanceResponse> checkOut(@PathVariable Long attendanceId) {
        Long memberId = currentUser.requireMemberId();
        Attendance attendance = attendanceService.checkOut(memberId, attendanceId);
        return ApiResponse.ok(toResponse(attendance));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<List<AttendanceResponse>> myAttendance() {
        Long memberId = currentUser.requireMemberId();
        List<AttendanceResponse> resp = attendanceService.listByMember(memberId).stream().map(this::toResponse).toList();
        return ApiResponse.ok(resp);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<AttendanceResponse>> list(@RequestParam(required = false) Long memberId) {
        List<Attendance> list = memberId == null ? attendanceService.listAll() : attendanceService.listByMember(memberId);
        return ApiResponse.ok(list.stream().map(this::toResponse).toList());
    }

    private AttendanceResponse toResponse(Attendance a) {
        return new AttendanceResponse(a.getId(), a.getAttendanceDate(), a.getStatus().name(), a.getCheckInTime(), a.getCheckOutTime());
    }
}
