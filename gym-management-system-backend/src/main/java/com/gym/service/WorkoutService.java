package com.gym.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.CreateWorkoutPlanRequest;
import com.gym.dto.ExerciseRequest;
import com.gym.dto.ExerciseResponse;
import com.gym.dto.UserResponse;
import com.gym.dto.WorkoutPlanResponse;
import com.gym.model.Exercise;
import com.gym.model.Member;
import com.gym.model.Trainer;
import com.gym.model.WorkoutPlan;
import com.gym.repository.ExerciseRepository;
import com.gym.repository.MemberRepository;
import com.gym.repository.TrainerRepository;
import com.gym.repository.WorkoutPlanRepository;

@Service
public class WorkoutService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;

    public WorkoutService(WorkoutPlanRepository workoutPlanRepository, ExerciseRepository exerciseRepository,
            MemberRepository memberRepository, TrainerRepository trainerRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseRepository = exerciseRepository;
        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
    }

    public WorkoutPlanResponse createWorkoutPlan(CreateWorkoutPlanRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));

        WorkoutPlan plan = new WorkoutPlan();
        plan.setPlanName(request.getPlanName());
        plan.setDescription(request.getDescription());
        plan.setFitnessGoal(request.getFitnessGoal());
        plan.setDifficultyLevel(request.getDifficultyLevel());
        plan.setStartDate(request.getStartDate());
        plan.setEndDate(request.getEndDate());
        plan.setDurationWeeks(request.getDurationWeeks());
        plan.setMember(member);
        plan.setTrainer(trainer);
        plan.setIsActive(true);

        WorkoutPlan saved = workoutPlanRepository.save(plan);
        return toWorkoutPlanResponse(saved);
    }

    public ExerciseResponse assignExercise(ExerciseRequest request) {
        WorkoutPlan plan = workoutPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout plan not found"));

        Exercise exercise = new Exercise();
        exercise.setExerciseName(request.getExerciseName());
        exercise.setBodyPart(request.getBodyPart());
        exercise.setEquipmentRequired(request.getEquipmentRequired());
        exercise.setSets(request.getSets());
        exercise.setReps(request.getReps());
        exercise.setDurationMinutes(request.getDurationMinutes());
        exercise.setInstructions(request.getInstructions());
        exercise.setVideoUrl(request.getVideoUrl());
        exercise.setWorkoutPlan(plan);
        exercise.setIsActive(true);

        Exercise saved = exerciseRepository.save(exercise);
        return toExerciseResponse(saved);
    }

    public List<WorkoutPlanResponse> getWorkoutPlansByMember(String memberId) {
        List<WorkoutPlan> plans = workoutPlanRepository.findActiveWorkoutPlansByMember(memberId);
        return plans.stream().map(this::toWorkoutPlanResponse).collect(Collectors.toList());
    }

    public List<ExerciseResponse> getExercisesByPlan(String planId) {
        List<Exercise> exercises = exerciseRepository.findActiveExercisesByWorkoutPlan(planId);
        return exercises.stream().map(this::toExerciseResponse).collect(Collectors.toList());
    }

    public WorkoutPlanResponse getWorkoutPlanById(String planId) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout plan not found"));
        return toWorkoutPlanResponse(plan);
    }

    public List<UserResponse> getTrainerAssignedMembers(String trainerId) {
        // Verify trainer exists
        trainerRepository.findById(trainerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));

        // Get all workout plans for this trainer and extract unique members
        List<WorkoutPlan> workoutPlans = workoutPlanRepository.findByTrainerUserId(trainerId);
        
        return workoutPlans.stream()
                .map(WorkoutPlan::getMember)
                .distinct()
                .map(member -> UserResponse.builder()
                        .userId(member.getUserId())
                        .name(member.getName())
                        .email(member.getEmail())
                        .phone(member.getPhone())
                        .role("MEMBER")
                        .build())
                .collect(Collectors.toList());
    }

    private WorkoutPlanResponse toWorkoutPlanResponse(WorkoutPlan plan) {
        return WorkoutPlanResponse.builder()
                .planId(plan.getPlanId())
                .planName(plan.getPlanName())
                .description(plan.getDescription())
                .fitnessGoal(plan.getFitnessGoal())
                .difficultyLevel(plan.getDifficultyLevel())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .durationWeeks(plan.getDurationWeeks())
                .isActive(plan.getIsActive())
                .createdDate(plan.getCreatedDate())
                .memberId(plan.getMember().getUserId())
                .trainerName(plan.getTrainer().getName())
                .exerciseCount(plan.getExercises().size())
                .build();
    }

    private ExerciseResponse toExerciseResponse(Exercise exercise) {
        return ExerciseResponse.builder()
                .exerciseId(exercise.getExerciseId())
                .exerciseName(exercise.getExerciseName())
                .bodyPart(exercise.getBodyPart())
                .equipmentRequired(exercise.getEquipmentRequired())
                .sets(exercise.getSets())
                .reps(exercise.getReps())
                .durationMinutes(exercise.getDurationMinutes())
                .instructions(exercise.getInstructions())
                .videoUrl(exercise.getVideoUrl())
                .isActive(exercise.getIsActive())
                .build();
    }
}
