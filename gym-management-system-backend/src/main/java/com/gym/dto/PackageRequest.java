package com.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageRequest {
    
    private String packageName;
    
    private String description;
    
    private Integer durationMonths;
    
    private Double price;
    
    private String benefits;
    
    private Boolean isActive;
}
