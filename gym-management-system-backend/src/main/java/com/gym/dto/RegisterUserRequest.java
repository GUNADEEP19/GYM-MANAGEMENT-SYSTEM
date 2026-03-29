package com.gym.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    private String name;
    private String email;
    private String phone;
    private UserType userType;
}
