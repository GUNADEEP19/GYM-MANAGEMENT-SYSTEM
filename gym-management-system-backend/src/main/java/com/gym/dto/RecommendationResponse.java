package com.gym.dto;

import java.util.List;

public class RecommendationResponse {
    private String memberId;
    private String memberName;
    private double currentBmi;
    private String selectedStrategy;
    private String advisedGoal;
    private int recommendedDurationWeeks;
    private List<String> recommendedExercises;

    public RecommendationResponse() {
    }

    public RecommendationResponse(String memberId, String memberName, double currentBmi, String selectedStrategy,
                                  String advisedGoal, int recommendedDurationWeeks, List<String> recommendedExercises) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.currentBmi = currentBmi;
        this.selectedStrategy = selectedStrategy;
        this.advisedGoal = advisedGoal;
        this.recommendedDurationWeeks = recommendedDurationWeeks;
        this.recommendedExercises = recommendedExercises;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public double getCurrentBmi() {
        return currentBmi;
    }

    public void setCurrentBmi(double currentBmi) {
        this.currentBmi = currentBmi;
    }

    public String getSelectedStrategy() {
        return selectedStrategy;
    }

    public void setSelectedStrategy(String selectedStrategy) {
        this.selectedStrategy = selectedStrategy;
    }

    public String getAdvisedGoal() {
        return advisedGoal;
    }

    public void setAdvisedGoal(String advisedGoal) {
        this.advisedGoal = advisedGoal;
    }

    public int getRecommendedDurationWeeks() {
        return recommendedDurationWeeks;
    }

    public void setRecommendedDurationWeeks(int recommendedDurationWeeks) {
        this.recommendedDurationWeeks = recommendedDurationWeeks;
    }

    public List<String> getRecommendedExercises() {
        return recommendedExercises;
    }

    public void setRecommendedExercises(List<String> recommendedExercises) {
        this.recommendedExercises = recommendedExercises;
    }
}
