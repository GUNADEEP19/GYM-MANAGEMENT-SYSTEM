package com.gym.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gym.model.WorkoutPlan;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, String> {

    List<WorkoutPlan> findByMemberId(String memberId);

    List<WorkoutPlan> findByTrainerId(String trainerId);

    List<WorkoutPlan> findByMemberIdAndIsActiveTrue(String memberId);

    @Query("SELECT wp FROM WorkoutPlan wp WHERE wp.member.userId = :memberId AND wp.isActive = true")
    List<WorkoutPlan> findActiveWorkoutPlansByMember(@Param("memberId") String memberId);
}
