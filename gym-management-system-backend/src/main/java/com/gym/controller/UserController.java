package com.gym.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.ForgotPasswordRequest;
import com.gym.dto.LoginUserRequest;
import com.gym.dto.RegisterUserRequest;
import com.gym.dto.UserResponse;
import com.gym.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        return ApiResponse.success("User registered successfully", userService.registerUser(request));
    }

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginUserRequest request) {
        return ApiResponse.success("Login successful", userService.loginUser(request));
    }

    @PostMapping("/password/forgot")
    public ApiResponse<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request);
        return ApiResponse.success("Password reset successful", null);
    }

    @GetMapping("/api/trainers")
    public ApiResponse<List<UserResponse>> getAllTrainers() {
        return ApiResponse.success("Trainers retrieved", userService.getAllTrainers());
    }
}
