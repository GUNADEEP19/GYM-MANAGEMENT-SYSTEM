package com.gym.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.ApiResponse;
import com.gym.dto.RegisterRequest;
import com.gym.dto.UserSummary;
import com.gym.model.AppUser;
import com.gym.model.UserRole;
import com.gym.repository.AppUserRepository;
import com.gym.service.MemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminUserController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    public AdminUserController(AppUserRepository userRepository, PasswordEncoder passwordEncoder, MemberService memberService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberService = memberService;
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserSummary> createUser(@Valid @RequestBody RegisterRequest request) {
        UserRole role = request.userType() == null ? UserRole.MEMBER : UserRole.fromValue(request.userType());
        if (role == UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create ADMIN via API");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        if (userRepository.existsByPhone(request.phone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone already exists");
        }

        Long memberId = null;
        if (role == UserRole.MEMBER) {
            memberId = memberService.createForAuthUser(request.name(), request.email(), request.phone(), request.trainerUserId()).getId();
        }

        AppUser user = AppUser.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .role(role)
                .passwordHash(passwordEncoder.encode(request.password()))
                .memberId(memberId)
                .build();

        AppUser saved = userRepository.save(user);
        return ApiResponse.ok(new UserSummary(saved.getId(), saved.getName(), saved.getEmail(), saved.getPhone(), saved.getRole().name()));
    }

    @GetMapping("/trainers")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserSummary>> listTrainers() {
        List<UserSummary> trainers = userRepository.findByRole(UserRole.TRAINER).stream()
                .map(u -> new UserSummary(u.getId(), u.getName(), u.getEmail(), u.getPhone(), u.getRole().name()))
                .toList();
        return ApiResponse.ok(trainers);
    }
}
