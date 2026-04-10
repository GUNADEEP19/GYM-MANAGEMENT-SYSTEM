package com.gym.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.ApiResponse;
import com.gym.dto.AuthResponse;
import com.gym.dto.LoginRequest;
import com.gym.dto.RegisterRequest;
import com.gym.model.AppUser;
import com.gym.model.UserRole;
import com.gym.repository.AppUserRepository;
import com.gym.security.JwtService;
import com.gym.service.MemberService;

import jakarta.validation.Valid;

@RestController
@Validated
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MemberService memberService;

    public AuthController(AppUserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            MemberService memberService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ApiResponse<Object> register(@Valid @RequestBody RegisterRequest request) {
        // Public registration is MEMBER-only. Trainers/Admins are created by ADMIN.
        UserRole role = UserRole.MEMBER;

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        if (userRepository.existsByPhone(request.phone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone already exists");
        }

        AppUser user = new AppUser();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        // Create Member profile and link it.
        Long memberId = memberService.createForAuthUser(request.name(), request.email(), request.phone(), request.trainerUserId())
                .getId();
        user.setMemberId(memberId);

        userRepository.save(user);
        return ApiResponse.ok("Registered successfully", null);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AppUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        AuthResponse resp = new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole().name(), token);
        return ApiResponse.ok(resp);
    }
}
