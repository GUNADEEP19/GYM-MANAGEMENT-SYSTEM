package com.gym.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.CreateExerciseRequest;
import com.gym.dto.CreateWorkoutPlanRequest;
import com.gym.model.Exercise;
import com.gym.model.Member;
import com.gym.model.WorkoutPlan;
import com.gym.repository.ExerciseRepository;
import com.gym.repository.MemberRepository;
import com.gym.repository.WorkoutPlanRepository;

@Service
public class WorkoutService {

    private final WorkoutPlanRepository planRepository;
    private final ExerciseRepository exerciseRepository;
    private final MemberRepository memberRepository;

    public WorkoutService(WorkoutPlanRepository planRepository, ExerciseRepository exerciseRepository,
            MemberRepository memberRepository) {
        this.planRepository = planRepository;
        this.exerciseRepository = exerciseRepository;
        this.memberRepository = memberRepository;
    }

    public WorkoutPlan createPlan(Long trainerUserId, CreateWorkoutPlanRequest request) {
        Long memberId = Objects.requireNonNull(request.memberId(), "memberId");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        if (member.getTrainerUserId() != null && !member.getTrainerUserId().equals(trainerUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Member is not assigned to this trainer");
        }

        WorkoutPlan plan = new WorkoutPlan();
        plan.setMemberId(member.getId());
        plan.setTrainerUserId(trainerUserId);
        plan.setPlanName(request.planName());
        plan.setDescription(request.description());
        plan.setDurationWeeks(request.durationWeeks());
        plan.setDifficultyLevel(request.difficultyLevel());
        plan.setCreatedAt(LocalDateTime.now());
        WorkoutPlan saved = planRepository.save(plan);

        if (request.exercises() != null) {
            for (CreateExerciseRequest ex : request.exercises()) {
                Exercise e = new Exercise();
                e.setPlan(saved);
                e.setExerciseName(ex.exerciseName());
                e.setSets(ex.sets());
                e.setReps(ex.reps());
                e.setBodyPart(ex.bodyPart());
                e.setInstructions(ex.instructions());
                exerciseRepository.save(e);
            }
        }
        return saved;
    }

    public List<WorkoutPlan> getPlansForMember(Long memberId) {
        return planRepository.findByMemberId(memberId);
    }

    public List<WorkoutPlan> getPlansForTrainer(Long trainerUserId) {
        return planRepository.findByTrainerUserId(trainerUserId);
    }

    public WorkoutPlan requirePlanForMember(Long planId, Long memberId) {
        Objects.requireNonNull(planId, "planId");
        WorkoutPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        if (!plan.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot access another member's plan");
        }
        return plan;
    }

    public List<Exercise> getExercisesForPlan(Long planId) {
        return exerciseRepository.findByPlanId(planId);
    }

    public long countExercises(Long planId) {
        return exerciseRepository.countByPlanId(planId);
    }
}
