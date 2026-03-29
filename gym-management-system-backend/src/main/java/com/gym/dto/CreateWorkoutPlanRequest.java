package com.gym.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class CreateWorkoutPlanRequest {
    private String planName;
    private String description;
    private String fitnessGoal;
    private String difficultyLevel;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer durationWeeks;
    private String memberId;
    private String trainerId;
}
