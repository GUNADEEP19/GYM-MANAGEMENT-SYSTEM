package com.gym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByPlanId(Long planId);
    long countByPlanId(Long planId);
}
