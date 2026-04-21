package com.gym.service.recommendation;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.RecommendationResponse;
import com.gym.model.Member;
import com.gym.model.ProgressRecord;
import com.gym.repository.MemberRepository;
import com.gym.service.ProgressService;

@Service
public class RecommendationService {

    private final ProgressService progressService;
    private final MemberRepository memberRepository;
    private final WeightLossStrategy weightLoss;
    private final MuscleGainStrategy muscleGain;
    private final GeneralFitnessStrategy generalFitness;

    public RecommendationService(ProgressService progressService, MemberRepository memberRepository,
            WeightLossStrategy weightLoss, MuscleGainStrategy muscleGain, GeneralFitnessStrategy generalFitness) {
        this.progressService = progressService;
        this.memberRepository = memberRepository;
        this.weightLoss = weightLoss;
        this.muscleGain = muscleGain;
        this.generalFitness = generalFitness;
    }

    public RecommendationResponse recommend(Long memberId) {
        Objects.requireNonNull(memberId, "memberId");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        ProgressRecord latest = progressService.latestForMember(memberId);
        
        double bmi;
        if (latest == null) {
            // Fallback for demo purposes so it always shows a value
            bmi = 22.0; 
        } else {
            bmi = latest.getBmi();
        }

        WorkoutRecommendationStrategy strategy;
        if (bmi >= 25.0) {
            strategy = weightLoss;
        } else if (bmi < 18.5) {
            strategy = muscleGain;
        } else {
            strategy = generalFitness;
        }

        return new RecommendationResponse(
                member.getName(),
                bmi,
                strategy.name(),
                strategy.advisedGoal(),
                strategy.recommendedDurationWeeks(),
                strategy.recommendedExercises());
    }
}
