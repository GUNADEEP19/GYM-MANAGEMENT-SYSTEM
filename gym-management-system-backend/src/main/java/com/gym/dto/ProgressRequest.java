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
public class ProgressRequest {
    private Integer weekNumber;
    private Integer exercisesDone;
    private String progressNotes;
    private String memberId;
    private String planId;
}
