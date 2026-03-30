package com.gym.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.LoginUserRequest;
import com.gym.dto.RegisterUserRequest;
import com.gym.dto.UserResponse;
import com.gym.dto.UserType;
import com.gym.model.Admin;
import com.gym.model.Member;
import com.gym.model.Trainer;
import com.gym.model.User;
import com.gym.repository.UserRepository;
import com.gym.security.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public UserResponse registerUser(RegisterUserRequest request) {
        if (request.getUserType() == null) {
            log.error("Registration failed: userType is required");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userType is required");
        }

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            log.warn("Registration failed: Email already registered - {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        });

        User user = createUserByType(request.getUserType());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User saved = userRepository.save(user);
        log.info("User registered successfully with email: {} as role: {}", saved.getEmail(), request.getUserType());
        return toResponse(saved);
    }

    public UserResponse loginUser(LoginUserRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());
        User user = userRepository
                .findByEmailAndPhone(request.getEmail(), request.getPhone())
                .orElseThrow(() -> {
                    log.error("Login attempt failed for email: {} - Invalid credentials", request.getEmail());
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
                });

        String token = jwtService.generateToken(user.getEmail());
        log.info("User logged in successfully: {}", request.getEmail());
        return toResponse(user, token);
    }

    private User createUserByType(UserType userType) {
        return switch (userType) {
            case MEMBER -> new Member();
            case TRAINER -> new Trainer();
            case ADMIN -> new Admin();
        };
    }

    private UserResponse toResponse(User user) {
        return toResponse(user, null);
    }

    private UserResponse toResponse(User user, String token) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getClass().getSimpleName())
                .token(token)
                .build();
    }
}
