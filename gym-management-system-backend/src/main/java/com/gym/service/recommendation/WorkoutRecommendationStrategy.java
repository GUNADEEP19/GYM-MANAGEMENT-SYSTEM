package com.gym.service.recommendation;

import com.gym.dto.RecommendationResponse;
import com.gym.model.Member;
import com.gym.model.Progress;

/**
 * Strategy interface for calculating workout recommendations based on member progress.
 * Using the Strategy Pattern for OOAD bonus points!
 */
public interface WorkoutRecommendationStrategy {
    
    /**
     * Determines whether this strategy is applicable for the given progress metrics.
     * @param progress The latest progress metrics for the member
     * @return true if applicable, false otherwise
     */
    boolean isApplicable(Progress progress);
    
    /**
     * Generates a workout recommendation based on the member's profile and progress.
     * @param member The member requesting recommendation
     * @param progress The member's latest progress data
     * @return RecommendationResponse containing strategy-specific advice
     */
    RecommendationResponse recommend(Member member, Progress progress);
}
