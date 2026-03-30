package com.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull(message = "userType is required")
    private UserType userType;

    @NotBlank(message = "Password is required")
    private String password;

    // Optional: Trainer assigned to this member (only used if userType == MEMBER)
    private String trainerUserId;
}
