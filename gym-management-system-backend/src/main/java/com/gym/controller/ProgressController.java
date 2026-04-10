package com.gym.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.ProgressRecordResponse;
import com.gym.dto.ProgressUpdateRequest;
import com.gym.model.ProgressRecord;
import com.gym.security.CurrentUser;
import com.gym.service.MemberService;
import com.gym.service.ProgressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/progress")
@Validated
public class ProgressController {

    private final ProgressService progressService;
    private final CurrentUser currentUser;
    private final MemberService memberService;

    public ProgressController(ProgressService progressService, CurrentUser currentUser, MemberService memberService) {
        this.progressService = progressService;
        this.currentUser = currentUser;
        this.memberService = memberService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<List<ProgressRecordResponse>> myRecords() {
        Long memberId = currentUser.requireMemberId();
        List<ProgressRecordResponse> resp = progressService.listForMember(memberId).stream().map(this::toResp).toList();
        return ApiResponse.ok(resp);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<ProgressRecordResponse> update(@Valid @RequestBody ProgressUpdateRequest request) {
        Long memberId = currentUser.requireMemberId();
        ProgressRecord saved = progressService.record(memberId, request);
        return ApiResponse.ok(toResp(saved));
    }

    @GetMapping("/trainer/member/{memberId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ApiResponse<List<ProgressRecordResponse>> memberProgressForTrainer(@PathVariable Long memberId) {
        Long trainerUserId = currentUser.requireUser().getId();
        memberService.requireAssignedToTrainer(memberId, trainerUserId);
        List<ProgressRecordResponse> resp = progressService.listForMember(memberId).stream().map(this::toResp).toList();
        return ApiResponse.ok(resp);
    }

    @GetMapping("/trainer/member/{memberId}/latest")
    @PreAuthorize("hasRole('TRAINER')")
    public ApiResponse<ProgressRecordResponse> latestMemberProgressForTrainer(@PathVariable Long memberId) {
        Long trainerUserId = currentUser.requireUser().getId();
        memberService.requireAssignedToTrainer(memberId, trainerUserId);
        ProgressRecord latest = progressService.latestForMember(memberId);
        return ApiResponse.ok(latest == null ? null : toResp(latest));
    }

    private ProgressRecordResponse toResp(ProgressRecord r) {
        return new ProgressRecordResponse(r.getId(), r.getWeekNumber(), r.getExercisesDone(), r.getWeight(), r.getBmi(), r.getProgressNotes(), r.getPlan().getId(), r.getPlan().getPlanName());
    }
}
