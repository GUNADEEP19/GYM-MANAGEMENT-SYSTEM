package com.gym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.model.GymPackage;

public interface GymPackageRepository extends JpaRepository<GymPackage, Long> {
    List<GymPackage> findByActiveTrue();
}
