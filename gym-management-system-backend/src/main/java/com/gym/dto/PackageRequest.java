package com.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageRequest {
    @NotBlank(message = "Package name is required")
    private String packageName;

    private String description;

    @NotNull(message = "Duration months is required")
    @Positive(message = "Duration months must be positive")
    private Integer durationMonths;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    private String benefits;

    private Boolean isActive;
}
