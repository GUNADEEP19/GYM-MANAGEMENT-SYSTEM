package com.gym.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.ProgressRequest;
import com.gym.dto.ProgressResponse;
import com.gym.model.Member;
import com.gym.model.Progress;
import com.gym.model.WorkoutPlan;
import com.gym.repository.MemberRepository;
import com.gym.repository.ProgressRepository;
import com.gym.repository.WorkoutPlanRepository;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final MemberRepository memberRepository;
    private final WorkoutPlanRepository workoutPlanRepository;

    public ProgressService(ProgressRepository progressRepository, MemberRepository memberRepository,
            WorkoutPlanRepository workoutPlanRepository) {
        this.progressRepository = progressRepository;
        this.memberRepository = memberRepository;
        this.workoutPlanRepository = workoutPlanRepository;
    }

    public ProgressResponse updateProgress(ProgressRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        WorkoutPlan plan = workoutPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout plan not found"));

        Progress progress = new Progress();
        progress.setMember(member);
        progress.setWorkoutPlan(plan);
        progress.setWeekNumber(request.getWeekNumber());
        progress.setExercisesDone(request.getExercisesDone());
        progress.setWeight(request.getWeight());
        progress.setBmi(request.getBmi());
        progress.setProgressNotes(request.getProgressNotes());
        progress.setRecordedDate(LocalDateTime.now());

        Progress saved = progressRepository.save(progress);
        return toProgressResponse(saved);
    }

    public List<ProgressResponse> getProgressByMember(String memberId) {
        List<Progress> progressRecords = progressRepository.findProgressByMemberOrderByDateDesc(memberId);
        return progressRecords.stream().map(this::toProgressResponse).collect(Collectors.toList());
    }

    public List<ProgressResponse> getProgressByMemberAndPlan(String memberId, String planId) {
        List<Progress> progressRecords = progressRepository.findProgressByMemberAndPlanOrderByWeek(memberId, planId);
        return progressRecords.stream().map(this::toProgressResponse).collect(Collectors.toList());
    }

    public List<ProgressResponse> getProgressInDateRange(String memberId, LocalDateTime startDate,
            LocalDateTime endDate) {
        List<Progress> progressRecords = progressRepository.findProgressByMemberInDateRange(memberId, startDate,
                endDate);
        return progressRecords.stream().map(this::toProgressResponse).collect(Collectors.toList());
    }

    private ProgressResponse toProgressResponse(Progress progress) {
        return ProgressResponse.builder()
                .progressId(progress.getProgressId())
                .weekNumber(progress.getWeekNumber())
                .exercisesDone(progress.getExercisesDone())
                .weight(progress.getWeight())
                .bmi(progress.getBmi())
                .progressNotes(progress.getProgressNotes())
                .recordedDate(progress.getRecordedDate())
                .createdAt(progress.getCreatedAt())
                .updatedAt(progress.getUpdatedAt())
                .planName(progress.getWorkoutPlan().getPlanName())
                .build();
    }
}
