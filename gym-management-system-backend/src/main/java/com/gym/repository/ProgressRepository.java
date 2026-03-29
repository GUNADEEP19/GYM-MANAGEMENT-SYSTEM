package com.gym.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gym.model.Progress;

public interface ProgressRepository extends JpaRepository<Progress, String> {
    
    List<Progress> findByMemberId(String memberId);
    
    List<Progress> findByMemberIdAndWorkoutPlanId(String memberId, String workoutPlanId);
    
    @Query("SELECT p FROM Progress p WHERE p.member.userId = :memberId ORDER BY p.recordedDate DESC")
    List<Progress> findProgressByMemberOrderByDateDesc(@Param("memberId") String memberId);
    
    @Query("SELECT p FROM Progress p WHERE p.member.userId = :memberId AND p.workoutPlan.planId = :planId ORDER BY p.weekNumber ASC")
    List<Progress> findProgressByMemberAndPlanOrderByWeek(@Param("memberId") String memberId, @Param("planId") String planId);
    
    @Query("SELECT p FROM Progress p WHERE p.member.userId = :memberId AND p.recordedDate BETWEEN :startDate AND :endDate")
    List<Progress> findProgressByMemberInDateRange(@Param("memberId") String memberId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
