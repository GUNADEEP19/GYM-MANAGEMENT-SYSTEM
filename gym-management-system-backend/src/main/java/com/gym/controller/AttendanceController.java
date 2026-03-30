package com.gym.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.gym.dto.ApiResponse;
import com.gym.dto.AttendanceRequest;
import com.gym.dto.AttendanceResponse;
import com.gym.service.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/checkin")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AttendanceResponse> markCheckIn(@Valid @RequestBody AttendanceRequest request) {
        return ApiResponse.success("Checked in successfully", attendanceService.markCheckIn(request));
    }

    @PostMapping("/checkout/{attendanceId}")
    public ApiResponse<AttendanceResponse> markCheckOut(@PathVariable String attendanceId) {
        return ApiResponse.success("Checked out successfully", attendanceService.markCheckOut(attendanceId));
    }

    @GetMapping("/member/{memberId}")
    public ApiResponse<List<AttendanceResponse>> getAttendanceByMember(@PathVariable String memberId) {
        return ApiResponse.success("Attendance records retrieved", attendanceService.getAttendanceByMember(memberId));
    }

    @GetMapping("/member/{memberId}/range")
    public ApiResponse<List<AttendanceResponse>> getAttendanceByDateRange(@PathVariable String memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success("Attendance records retrieved", attendanceService.getAttendanceByDateRange(memberId, startDate, endDate));
    }

    @GetMapping("/member/{memberId}/count")
    public ApiResponse<Integer> getAttendanceCount(@PathVariable String memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success("Attendance count retrieved", attendanceService.getAttendanceCount(memberId, startDate, endDate));
    }
}
