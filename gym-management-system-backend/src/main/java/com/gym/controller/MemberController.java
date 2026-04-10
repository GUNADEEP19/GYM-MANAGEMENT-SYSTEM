package com.gym.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.ApiResponse;
import com.gym.dto.CreateMemberRequest;
import com.gym.dto.UpdateMemberStatusRequest;
import com.gym.dto.UserSummary;
import com.gym.model.Member;
import com.gym.model.UserRole;
import com.gym.repository.AppUserRepository;
import com.gym.security.CurrentUser;
import com.gym.service.MemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
@Validated
public class MemberController {

    private final MemberService memberService;
    private final CurrentUser currentUser;
    private final AppUserRepository userRepository;

    public MemberController(MemberService memberService, CurrentUser currentUser, AppUserRepository userRepository) {
        this.memberService = memberService;
        this.currentUser = currentUser;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Member> create(@Valid @RequestBody CreateMemberRequest request) {
        return ApiResponse.ok(memberService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Member>> list() {
        return ApiResponse.ok(memberService.list());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Member> get(@PathVariable Long id) {
        return ApiResponse.ok(memberService.getById(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Member> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateMemberStatusRequest request) {
        return ApiResponse.ok(memberService.updateStatus(id, request.status()));
    }

    @GetMapping("/me/trainer")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<UserSummary> myTrainer() {
        Long memberId = currentUser.requireMemberId();
        Member member = memberService.getById(memberId);

        Long trainerUserId = member.getTrainerUserId();
        if (trainerUserId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not assigned");
        }

        var trainer = userRepository.findById(trainerUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));

        if (trainer.getRole() != UserRole.TRAINER) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Assigned trainer user is not TRAINER");
        }

        return ApiResponse.ok(new UserSummary(
                trainer.getId(),
                trainer.getName(),
                trainer.getEmail(),
                trainer.getPhone(),
                trainer.getRole().name()));
    }
}
