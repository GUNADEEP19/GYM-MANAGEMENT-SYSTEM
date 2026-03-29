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
    public AttendanceResponse markCheckIn(@RequestBody AttendanceRequest request) {
        return attendanceService.markCheckIn(request);
    }

    @PostMapping("/checkout/{attendanceId}")
    public AttendanceResponse markCheckOut(@PathVariable String attendanceId) {
        return attendanceService.markCheckOut(attendanceId);
    }

    @GetMapping("/member/{memberId}")
    public List<AttendanceResponse> getAttendanceByMember(@PathVariable String memberId) {
        return attendanceService.getAttendanceByMember(memberId);
    }

    @GetMapping("/member/{memberId}/range")
    public List<AttendanceResponse> getAttendanceByDateRange(@PathVariable String memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return attendanceService.getAttendanceByDateRange(memberId, startDate, endDate);
    }

    @GetMapping("/member/{memberId}/count")
    public Integer getAttendanceCount(@PathVariable String memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return attendanceService.getAttendanceCount(memberId, startDate, endDate);
    }
}
