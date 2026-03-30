package com.gym.service.recommendation;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.gym.dto.RecommendationResponse;
import com.gym.model.Member;
import com.gym.model.Progress;

@Component
public class WeightLossStrategy implements WorkoutRecommendationStrategy {

    @Override
    public boolean isApplicable(Progress progress) {
        // BMI >= 25 is considered overweight/obese
        return progress != null && progress.getBmi() != null && progress.getBmi() >= 25.0;
    }

    @Override
    public RecommendationResponse recommend(Member member, Progress progress) {
        return new RecommendationResponse(
            member.getUserId(),
            member.getName(),
            progress.getBmi(),
            "WeightLossStrategy",
            "Focus on a caloric deficit and high-intensity cardiovascular training.",
            12,
            Arrays.asList("Treadmill Running (30 mins)", "Burpees (3 sets of 15)", "Jump Rope (15 mins)", "Cycling (20 mins)")
        );
    }
}
