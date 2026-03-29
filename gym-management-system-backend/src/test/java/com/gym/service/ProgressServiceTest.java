package com.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.ProgressRequest;
import com.gym.model.Member;
import com.gym.model.Progress;
import com.gym.model.WorkoutPlan;
import com.gym.repository.MemberRepository;
import com.gym.repository.ProgressRepository;
import com.gym.repository.WorkoutPlanRepository;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @InjectMocks
    private ProgressService progressService;

    @Test
    void updateProgressThrowsWhenMemberNotFound() {
        ProgressRequest request = new ProgressRequest();
        request.setMemberId("invalid-member");
        request.setPlanId("plan-1");

        when(memberRepository.findById("invalid-member")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> progressService.updateProgress(request));
    }

    @Test
    void updateProgressThrowsWhenPlanNotFound() {
        ProgressRequest request = new ProgressRequest();
        request.setMemberId("member-1");
        request.setPlanId("invalid-plan");

        Member member = new Member();
        member.setUserId("member-1");

        when(memberRepository.findById("member-1")).thenReturn(Optional.of(member));
        when(workoutPlanRepository.findById("invalid-plan")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> progressService.updateProgress(request));
    }

    @Test
    void updateProgressSuccessfully() {
        ProgressRequest request = new ProgressRequest();
        request.setMemberId("member-1");
        request.setPlanId("plan-1");
        request.setWeekNumber(1);
        request.setExercisesDone(5);
        request.setProgressNotes("Good progress");

        Member member = new Member();
        member.setUserId("member-1");

        WorkoutPlan plan = new WorkoutPlan();
        plan.setPlanId("plan-1");
        plan.setPlanName("Weight Loss Plan");

        Progress savedProgress = new Progress();
        savedProgress.setProgressId("progress-1");
        savedProgress.setWeekNumber(1);
        savedProgress.setExercisesDone(5);

        when(memberRepository.findById("member-1")).thenReturn(Optional.of(member));
        when(workoutPlanRepository.findById("plan-1")).thenReturn(Optional.of(plan));
        when(progressRepository.save(any(Progress.class))).thenReturn(savedProgress);

        var response = progressService.updateProgress(request);

        assertNotNull(response);
        assertEquals("progress-1", response.getProgressId());
        assertEquals(1, response.getWeekNumber());
        assertEquals(5, response.getExercisesDone());
        verify(progressRepository).save(any(Progress.class));
    }
}
