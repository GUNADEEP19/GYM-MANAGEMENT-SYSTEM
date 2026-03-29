package com.gym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gym.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, String> {
    
    List<Exercise> findByWorkoutPlanId(String workoutPlanId);
    
    List<Exercise> findByWorkoutPlanIdAndIsActiveTrue(String workoutPlanId);
    
    @Query("SELECT e FROM Exercise e WHERE e.workoutPlan.planId = :planId AND e.isActive = true")
    List<Exercise> findActiveExercisesByWorkoutPlan(@Param("planId") String planId);
    
    @Query("SELECT e FROM Exercise e WHERE e.workoutPlan.planId = :planId")
    List<Exercise> findAllExercisesByWorkoutPlan(@Param("planId") String planId);
}
