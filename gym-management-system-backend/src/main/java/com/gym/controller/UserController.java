package com.gym.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public UserResponse register(@Valid @RequestBody RegisterUserRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginUserRequest request) {
        return userService.loginUser(request);
    }
}
