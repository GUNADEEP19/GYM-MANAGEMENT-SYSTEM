package com.gym.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.RecommendationResponse;
import com.gym.security.CurrentUser;
import com.gym.service.recommendation.RecommendationService;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private final CurrentUser currentUser;
    private final RecommendationService recommendationService;

    public RecommendationController(CurrentUser currentUser, RecommendationService recommendationService) {
        this.currentUser = currentUser;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<RecommendationResponse> me() {
        Long memberId = currentUser.requireMemberId();
        return ApiResponse.ok(recommendationService.recommend(memberId));
    }
}
