package com.gym.service.recommendation;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.gym.dto.RecommendationResponse;
import com.gym.model.Member;
import com.gym.model.Progress;

@Component
public class MuscleGainStrategy implements WorkoutRecommendationStrategy {

    @Override
    public boolean isApplicable(Progress progress) {
        // BMI < 18.5 is considered underweight
        return progress != null && progress.getBmi() != null && progress.getBmi() < 18.5;
    }

    @Override
    public RecommendationResponse recommend(Member member, Progress progress) {
        return new RecommendationResponse(
            member.getUserId(),
            member.getName(),
            progress.getBmi(),
            "MuscleGainStrategy",
            "Focus on a caloric surplus and progressive overload with heavy compound lifts.",
            16,
            Arrays.asList("Barbell Squat (4 sets of 8)", "Deadlift (3 sets of 5)", "Bench Press (4 sets of 8)", "Overhead Press (3 sets of 10)")
        );
    }
}
