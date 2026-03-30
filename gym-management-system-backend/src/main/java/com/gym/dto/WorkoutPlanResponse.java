package com.gym.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlanResponse {
    private String planId;
    private String planName;
    private String description;
    private String fitnessGoal;
    private String difficultyLevel;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer durationWeeks;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private String memberId;
    private String trainerName;
    private Integer exerciseCount;
}
