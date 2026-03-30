package com.gym.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.RecommendationResponse;
import com.gym.service.recommendation.RecommendationService;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getRecommendation(@PathVariable String memberId) {
        RecommendationResponse response = recommendationService.generateRecommendation(memberId);
        return ResponseEntity.ok(ApiResponse.success("Workout recommendation generated successfully", response));
    }
}
