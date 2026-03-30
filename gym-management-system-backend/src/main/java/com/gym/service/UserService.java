package com.gym.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.ForgotPasswordRequest;
import com.gym.dto.LoginUserRequest;
import com.gym.dto.RegisterUserRequest;
import com.gym.dto.UserResponse;
import com.gym.dto.UserType;
import com.gym.model.Admin;
import com.gym.model.Member;
import com.gym.model.Trainer;
import com.gym.model.User;
import com.gym.model.WorkoutPlan;
import com.gym.repository.MemberRepository;
import com.gym.repository.TrainerRepository;
import com.gym.repository.UserRepository;
import com.gym.repository.WorkoutPlanRepository;
import com.gym.security.JwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;
    private final WorkoutPlanRepository workoutPlanRepository;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder,
            TrainerRepository trainerRepository, MemberRepository memberRepository,
            WorkoutPlanRepository workoutPlanRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.trainerRepository = trainerRepository;
        this.memberRepository = memberRepository;
        this.workoutPlanRepository = workoutPlanRepository;
    }

    @Transactional
    public UserResponse registerUser(RegisterUserRequest request) {
        if (request.getUserType() == null) {
            log.error("Registration failed: userType is required");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userType is required");
        }

        if (request.getUserType() == UserType.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin cannot be created via registration");
        }

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            log.warn("Registration failed: Email already registered - {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        });

        User user = createUserByType(request.getUserType());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        // If a trainer is assigned to this member, create initial workout plan
        if (request.getUserType() == UserType.MEMBER && request.getTrainerUserId() != null
                && !request.getTrainerUserId().isBlank()) {
            Trainer trainer = trainerRepository.findById(request.getTrainerUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));

            Member member = (Member) saved;

            // Create initial workout plan
            WorkoutPlan plan = new WorkoutPlan();
            plan.setPlanName("Initial Plan");
            plan.setDescription("Initial workout plan assigned during registration");
            plan.setFitnessGoal("General Fitness");
            plan.setDifficultyLevel("Beginner");
            plan.setMember(member);
            plan.setTrainer(trainer);
            plan.setIsActive(true);

            workoutPlanRepository.save(plan);
            log.info("Initial workout plan created for member: {} with trainer: {}", saved.getEmail(),
                    trainer.getEmail());
        }

        log.info("User registered successfully with email: {} as role: {}", saved.getEmail(), request.getUserType());
        return toResponse(saved);
    }

    public UserResponse loginUser(LoginUserRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Login attempt failed for email: {} - Invalid credentials", request.getEmail());
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
                });

        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password not set. Contact admin.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        log.info("User logged in successfully: {}", request.getEmail());
        return toResponse(user, token);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository
                .findByEmailAndPhone(request.getEmail(), request.getPhone())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid details"));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
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

    public List<UserResponse> getAllTrainers() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof Trainer)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
