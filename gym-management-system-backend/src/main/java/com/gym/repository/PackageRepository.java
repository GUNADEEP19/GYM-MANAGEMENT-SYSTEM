package com.gym.repository;

import com.gym.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, String> {
    
    /**
     * Find active packages
     * @return List of active packages
     */
    @Query("SELECT p FROM Package p WHERE p.isActive = true")
    List<Package> findActivePackages();
    
    /**
     * Find package by name
     * @param packageName Package name
     * @return Optional of Package
     */
    Optional<Package> findByPackageName(String packageName);
    
    /**
     * Find packages by duration
     * @param durationMonths Duration in months
     * @return List of packages with specified duration
     */
    @Query("SELECT p FROM Package p WHERE p.durationMonths = :duration")
    List<Package> findByDuration(@Param("duration") Integer durationMonths);
}
