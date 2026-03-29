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

import com.gym.dto.CreateWorkoutPlanRequest;
import com.gym.dto.ExerciseRequest;
import com.gym.dto.ExerciseResponse;
import com.gym.dto.WorkoutPlanResponse;
import com.gym.service.WorkoutService;

@RestController
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutPlanResponse createWorkoutPlan(@RequestBody CreateWorkoutPlanRequest request) {
        return workoutService.createWorkoutPlan(request);
    }

    @GetMapping("/{planId}")
    public WorkoutPlanResponse getWorkoutPlan(@PathVariable String planId) {
        return workoutService.getWorkoutPlanById(planId);
    }

    @GetMapping("/member/{memberId}")
    public List<WorkoutPlanResponse> getWorkoutPlansByMember(@PathVariable String memberId) {
        return workoutService.getWorkoutPlansByMember(memberId);
    }

    @PostMapping("/exercise/assign")
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciseResponse assignExercise(@RequestBody ExerciseRequest request) {
        return workoutService.assignExercise(request);
    }

    @GetMapping("/{planId}/exercises")
    public List<ExerciseResponse> getExercisesByPlan(@PathVariable String planId) {
        return workoutService.getExercisesByPlan(planId);
    }
}
