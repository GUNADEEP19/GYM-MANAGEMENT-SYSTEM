package com.gym.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gym.dto.ProgressUpdateRequest;
import com.gym.model.ProgressRecord;
import com.gym.model.WorkoutPlan;
import com.gym.repository.ProgressRecordRepository;

@Service
public class ProgressService {

    private final ProgressRecordRepository progressRepository;
    private final WorkoutService workoutService;

    public ProgressService(ProgressRecordRepository progressRepository, WorkoutService workoutService) {
        this.progressRepository = progressRepository;
        this.workoutService = workoutService;
    }

    public ProgressRecord record(Long memberId, ProgressUpdateRequest request) {
        WorkoutPlan plan = workoutService.requirePlanForMember(request.planId(), memberId);

        ProgressRecord pr = new ProgressRecord();
        pr.setMemberId(memberId);
        pr.setPlan(plan);
        pr.setWeekNumber(request.weekNumber());
        pr.setExercisesDone(request.exercisesDone());
        pr.setWeight(request.weight());
        pr.setBmi(request.bmi());
        pr.setProgressNotes(request.progressNotes());
        pr.setCreatedAt(LocalDateTime.now());
        return progressRepository.save(pr);
    }

    public List<ProgressRecord> listForMember(Long memberId) {
        return progressRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
    }

    public ProgressRecord latestForMember(Long memberId) {
        return progressRepository.findTopByMemberIdOrderByCreatedAtDesc(memberId).orElse(null);
    }
}
