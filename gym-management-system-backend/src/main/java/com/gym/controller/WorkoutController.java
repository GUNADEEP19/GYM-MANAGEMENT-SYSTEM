package com.gym.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.CreateWorkoutPlanRequest;
import com.gym.dto.ExerciseResponse;
import com.gym.dto.WorkoutPlanResponse;
import com.gym.model.WorkoutPlan;
import com.gym.security.CurrentUser;
import com.gym.service.WorkoutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/workouts")
@Validated
public class WorkoutController {

    private final WorkoutService workoutService;
    private final CurrentUser currentUser;

    public WorkoutController(WorkoutService workoutService, CurrentUser currentUser) {
        this.workoutService = workoutService;
        this.currentUser = currentUser;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('TRAINER')")
    public ApiResponse<WorkoutPlanResponse> create(@Valid @RequestBody CreateWorkoutPlanRequest request) {
        Long trainerId = currentUser.requireUser().getId();
        WorkoutPlan plan = workoutService.createPlan(trainerId, request);
        long count = workoutService.countExercises(plan.getId());
        return ApiResponse.ok(toResponse(plan, count));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<List<WorkoutPlanResponse>> myPlans() {
        Long memberId = currentUser.requireMemberId();
        List<WorkoutPlanResponse> resp = workoutService.getPlansForMember(memberId).stream()
                .map(p -> toResponse(p, workoutService.countExercises(p.getId())))
                .toList();
        return ApiResponse.ok(resp);
    }

    @GetMapping("/trainer/me")
    @PreAuthorize("hasRole('TRAINER')")
    public ApiResponse<List<WorkoutPlanResponse>> myTrainerPlans() {
        Long trainerId = currentUser.requireUser().getId();
        List<WorkoutPlanResponse> resp = workoutService.getPlansForTrainer(trainerId).stream()
                .map(p -> toResponse(p, workoutService.countExercises(p.getId())))
                .toList();
        return ApiResponse.ok(resp);
    }

    @GetMapping("/{planId}/exercises")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<List<ExerciseResponse>> exercises(@PathVariable Long planId) {
        Long memberId = currentUser.requireMemberId();
        workoutService.requirePlanForMember(planId, memberId);
        List<ExerciseResponse> resp = workoutService.getExercisesForPlan(planId).stream()
                .map(ex -> new ExerciseResponse(ex.getId(), ex.getExerciseName(), ex.getSets(), ex.getReps(), ex.getBodyPart(), ex.getInstructions()))
                .toList();
        return ApiResponse.ok(resp);
    }

    private WorkoutPlanResponse toResponse(WorkoutPlan p, long exerciseCount) {
        return new WorkoutPlanResponse(p.getId(), p.getPlanName(), p.getDescription(), p.getDurationWeeks(), p.getDifficultyLevel(), exerciseCount);
    }
}
