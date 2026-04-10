package com.gym.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.AssignMembershipRequest;
import com.gym.dto.MembershipValidityResponse;
import com.gym.model.Membership;
import com.gym.security.CurrentUser;
import com.gym.service.MembershipService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/memberships")
@Validated
public class MembershipController {

    private final MembershipService membershipService;
    private final CurrentUser currentUser;

    public MembershipController(MembershipService membershipService, CurrentUser currentUser) {
        this.membershipService = membershipService;
        this.currentUser = currentUser;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Membership> assign(@Valid @RequestBody AssignMembershipRequest request) {
        return ApiResponse.ok(membershipService.assign(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Membership>> list(@RequestParam(required = false) Long memberId) {
        if (memberId == null) {
            return ApiResponse.ok(membershipService.listAll());
        }
        return ApiResponse.ok(membershipService.listByMember(memberId));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<MembershipValidityResponse> myValidity() {
        Long memberId = currentUser.requireMemberId();
        LocalDate today = LocalDate.now();

        Membership latest = membershipService.latestForMember(memberId);
        boolean valid = membershipService.isActiveForMemberOn(memberId, today);

        return ApiResponse.ok(new MembershipValidityResponse(
                valid,
                latest != null ? latest.getStatus().name() : "NONE",
                latest != null ? latest.getStartDate() : null,
                latest != null ? latest.getEndDate() : null,
                latest != null && latest.getGymPackage() != null ? latest.getGymPackage().getId() : null,
                latest != null && latest.getGymPackage() != null ? latest.getGymPackage().getName() : null));
    }
}
