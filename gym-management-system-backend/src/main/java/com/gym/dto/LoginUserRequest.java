package com.gym.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {
    private String email;
    private String phone;
}
