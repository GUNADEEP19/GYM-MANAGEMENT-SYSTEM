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
public class ProgressResponse {
    private String progressId;
    private Integer weekNumber;
    private Integer exercisesDone;
    private String progressNotes;
    private LocalDateTime recordedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String planName;
}
