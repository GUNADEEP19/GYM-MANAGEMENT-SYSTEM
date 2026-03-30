package com.gym.controller;

import java.time.LocalDateTime;
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
import com.gym.dto.ProgressRequest;
import com.gym.dto.ProgressResponse;
import com.gym.service.ProgressService;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProgressResponse> updateProgress(@Valid @RequestBody ProgressRequest request) {
        return ApiResponse.success("Progress updated successfully", progressService.updateProgress(request));
    }

    @GetMapping("/member/{memberId}")
    public ApiResponse<List<ProgressResponse>> getProgressByMember(@PathVariable String memberId) {
        return ApiResponse.success("Progress retrieved", progressService.getProgressByMember(memberId));
    }

    @GetMapping("/member/{memberId}/plan/{planId}")
    public ApiResponse<List<ProgressResponse>> getProgressByMemberAndPlan(@PathVariable String memberId,
            @PathVariable String planId) {
        return ApiResponse.success("Progress retrieved", progressService.getProgressByMemberAndPlan(memberId, planId));
    }

    @GetMapping("/member/{memberId}/range")
    public ApiResponse<List<ProgressResponse>> getProgressInDateRange(@PathVariable String memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ApiResponse.success("Progress retrieved", progressService.getProgressInDateRange(memberId, startDate, endDate));
    }
}
