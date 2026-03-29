package com.gym.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String role;
}
