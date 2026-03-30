package com.gym.service.recommendation;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.gym.dto.RecommendationResponse;
import com.gym.model.Member;
import com.gym.model.Progress;

@Component
public class GeneralFitnessStrategy implements WorkoutRecommendationStrategy {

    @Override
    public boolean isApplicable(Progress progress) {
        // 18.5 <= BMI < 25 is considered healthy weight range
        if (progress == null || progress.getBmi() == null) {
            return false;
        }
        return progress.getBmi() >= 18.5 && progress.getBmi() < 25.0;
    }

    @Override
    public RecommendationResponse recommend(Member member, Progress progress) {
        return new RecommendationResponse(
            member.getUserId(),
            member.getName(),
            progress.getBmi(),
            "GeneralFitnessStrategy",
            "Maintain current BMI. Focus on balancing endurance, flexibility, and core strength.",
            8,
            Arrays.asList("Plank (3 sets)", "Dumbbell Lunges (3 sets of 12)", "Yoga Routine (20 mins)", "Push-ups (3 sets of 15)")
        );
    }
}
