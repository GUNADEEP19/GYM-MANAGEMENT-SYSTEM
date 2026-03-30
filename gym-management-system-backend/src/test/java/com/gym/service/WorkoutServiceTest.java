package com.gym.service;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.CreateWorkoutPlanRequest;
import com.gym.dto.ExerciseRequest;
import com.gym.model.Member;
import com.gym.model.Trainer;
import com.gym.model.WorkoutPlan;
import com.gym.repository.ExerciseRepository;
import com.gym.repository.MemberRepository;
import com.gym.repository.TrainerRepository;
import com.gym.repository.WorkoutPlanRepository;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private WorkoutService workoutService;

    @Test
    void createWorkoutPlanThrowsWhenMemberNotFound() {
        CreateWorkoutPlanRequest request = new CreateWorkoutPlanRequest();
        request.setMemberId("invalid-member");
        request.setTrainerId("trainer-1");

        when(memberRepository.findById("invalid-member")).thenReturn(Optional.empty());

        ResponseStatusException exception =
            assertThrows(ResponseStatusException.class, () -> workoutService.createWorkoutPlan(request));
        assertNotNull(exception);
    }

    @Test
    void createWorkoutPlanThrowsWhenTrainerNotFound() {
        CreateWorkoutPlanRequest request = new CreateWorkoutPlanRequest();
        request.setMemberId("member-1");
        request.setTrainerId("invalid-trainer");

        Member member = new Member();
        member.setUserId("member-1");

        when(memberRepository.findById("member-1")).thenReturn(Optional.of(member));
        when(trainerRepository.findById("invalid-trainer")).thenReturn(Optional.empty());

        ResponseStatusException exception =
            assertThrows(ResponseStatusException.class, () -> workoutService.createWorkoutPlan(request));
        assertNotNull(exception);
    }

    @Test
    void createWorkoutPlanSuccessfully() {
        CreateWorkoutPlanRequest request = new CreateWorkoutPlanRequest();
        request.setPlanName("Weight Loss Plan");
        request.setFitnessGoal("Weight Loss");
        request.setDifficultyLevel("Intermediate");
        request.setMemberId("member-1");
        request.setTrainerId("trainer-1");
        request.setDurationWeeks(8);

        Member member = new Member();
        member.setUserId("member-1");

        Trainer trainer = new Trainer();
        trainer.setUserId("trainer-1");
        trainer.setName("John Trainer");

        WorkoutPlan savedPlan = new WorkoutPlan();
        savedPlan.setPlanId("plan-1");
        savedPlan.setPlanName(request.getPlanName());
        savedPlan.setMember(member);
        savedPlan.setTrainer(trainer);
        savedPlan.setExercises(new ArrayList<>());

        when(memberRepository.findById("member-1")).thenReturn(Optional.of(member));
        when(trainerRepository.findById("trainer-1")).thenReturn(Optional.of(trainer));
        when(workoutPlanRepository.save(any(WorkoutPlan.class))).thenReturn(savedPlan);

        var response = workoutService.createWorkoutPlan(request);

        assertNotNull(response);
        assertEquals("plan-1", response.getPlanId());
        assertEquals("Weight Loss Plan", response.getPlanName());
        verify(workoutPlanRepository).save(any(WorkoutPlan.class));
    }

    @Test
    void assignExerciseThrowsWhenPlanNotFound() {
        ExerciseRequest request = new ExerciseRequest();
        request.setPlanId("invalid-plan");

        when(workoutPlanRepository.findById("invalid-plan")).thenReturn(Optional.empty());

        ResponseStatusException exception =
            assertThrows(ResponseStatusException.class, () -> workoutService.assignExercise(request));
        assertNotNull(exception);
        verifyNoInteractions(exerciseRepository);
    }
}
