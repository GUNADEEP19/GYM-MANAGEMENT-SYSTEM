package com.gym.dto;

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
public class ExerciseResponse {
    private String exerciseId;
    private String exerciseName;
    private String bodyPart;
    private String equipmentRequired;
    private Integer sets;
    private Integer reps;
    private Integer durationMinutes;
    private String instructions;
    private String videoUrl;
    private Boolean isActive;
}
