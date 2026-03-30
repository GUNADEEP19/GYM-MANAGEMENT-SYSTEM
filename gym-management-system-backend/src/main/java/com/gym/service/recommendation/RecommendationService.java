package com.gym.service.recommendation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.RecommendationResponse;
import com.gym.model.Member;
import com.gym.model.Progress;
import com.gym.repository.MemberRepository;
import com.gym.repository.ProgressRepository;

@Service
public class RecommendationService {

    private final List<WorkoutRecommendationStrategy> strategies;
    private final MemberRepository memberRepository;
    private final ProgressRepository progressRepository;

    @Autowired
    public RecommendationService(List<WorkoutRecommendationStrategy> strategies, 
                                 MemberRepository memberRepository, 
                                 ProgressRepository progressRepository) {
        this.strategies = strategies;
        this.memberRepository = memberRepository;
        this.progressRepository = progressRepository;
    }

    public RecommendationResponse generateRecommendation(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        List<Progress> progressRecords = progressRepository.findProgressByMemberOrderByDateDesc(memberId);
        
        Progress latestProgress = progressRecords.isEmpty() ? null : progressRecords.get(0);
        
        if (latestProgress == null || latestProgress.getBmi() == null) {
            return new RecommendationResponse(
                member.getUserId(),
                member.getName(),
                0.0,
                "NoStrategy",
                "Start by updating your progress and calculating your BMI so we can recommend a tailored strategy.",
                4,
                new ArrayList<>()
            );
        }

        for (WorkoutRecommendationStrategy strategy : strategies) {
            if (strategy.isApplicable(latestProgress)) {
                return strategy.recommend(member, latestProgress);
            }
        }
        
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No applicable strategy found for BMI: " + latestProgress.getBmi());
    }
}
