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

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse registerUser(RegisterUserRequest request) {
        if (request.getUserType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userType is required");
        }

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        });

        User user = createUserByType(request.getUserType());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse loginUser(LoginUserRequest request) {
        User user = userRepository
                .findByEmailAndPhone(request.getEmail(), request.getPhone())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        return toResponse(user);
    }

    private User createUserByType(UserType userType) {
        return switch (userType) {
            case MEMBER -> new Member();
            case TRAINER -> new Trainer();
            case ADMIN -> new Admin();
        };
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getClass().getSimpleName())
                .build();
    }
}
