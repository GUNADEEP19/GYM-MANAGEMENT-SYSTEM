package com.gym.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.UserSummary;
import com.gym.model.Member;
import com.gym.repository.MemberRepository;
import com.gym.security.CurrentUser;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final CurrentUser currentUser;
    private final MemberRepository memberRepository;

    public TrainerController(CurrentUser currentUser, MemberRepository memberRepository) {
        this.currentUser = currentUser;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/me/members")
    @PreAuthorize("hasRole('TRAINER')")
    public ApiResponse<List<UserSummary>> myMembers() {
        Long trainerUserId = currentUser.requireUser().getId();
        List<Member> members = memberRepository.findByTrainerUserId(trainerUserId);
        List<UserSummary> resp = members.stream()
                // We expose memberId as userId to keep frontend simple.
                .map(m -> new UserSummary(m.getId(), m.getName(), m.getEmail(), m.getPhone(), "MEMBER"))
                .toList();
        return ApiResponse.ok(resp);
    }
}
