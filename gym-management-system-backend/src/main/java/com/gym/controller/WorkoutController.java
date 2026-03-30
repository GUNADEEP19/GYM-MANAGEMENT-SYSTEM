package com.gym.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.CreateWorkoutPlanRequest;
import com.gym.dto.ExerciseRequest;
import com.gym.dto.ExerciseResponse;
import com.gym.dto.UserResponse;
import com.gym.dto.WorkoutPlanResponse;
import com.gym.service.WorkoutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WorkoutPlanResponse> createWorkoutPlan(@Valid @RequestBody CreateWorkoutPlanRequest request) {
        return ApiResponse.success("Workout plan created successfully", workoutService.createWorkoutPlan(request));
    }

    @GetMapping("/{planId}")
    public ApiResponse<WorkoutPlanResponse> getWorkoutPlan(@PathVariable String planId) {
        return ApiResponse.success("Workout plan retrieved", workoutService.getWorkoutPlanById(planId));
    }

    @GetMapping("/member/{memberId}")
    public ApiResponse<List<WorkoutPlanResponse>> getWorkoutPlansByMember(@PathVariable String memberId) {
        return ApiResponse.success("Workout plans retrieved", workoutService.getWorkoutPlansByMember(memberId));
    }

    @PostMapping("/exercise/assign")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ExerciseResponse> assignExercise(@Valid @RequestBody ExerciseRequest request) {
        return ApiResponse.success("Exercise assigned successfully", workoutService.assignExercise(request));
    }

    @GetMapping("/{planId}/exercises")
    public ApiResponse<List<ExerciseResponse>> getExercisesByPlan(@PathVariable String planId) {
        return ApiResponse.success("Exercises retrieved", workoutService.getExercisesByPlan(planId));
    }

    @GetMapping("/trainer/{trainerId}/members")
    public ApiResponse<List<UserResponse>> getTrainerAssignedMembers(@PathVariable String trainerId) {
        return ApiResponse.success("Assigned members retrieved", workoutService.getTrainerAssignedMembers(trainerId));
    }
}
