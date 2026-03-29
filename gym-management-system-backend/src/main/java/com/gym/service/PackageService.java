package com.gym.service;

import com.gym.dto.PackageRequest;
import com.gym.dto.PackageResponse;
import com.gym.model.Package;
import com.gym.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for Package Management
 */
@Service
public class PackageService {
    
    private final PackageRepository packageRepository;
    
    @Autowired
    public PackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }
    
    /**
     * Create a new package
     * @param packageRequest Package details
     * @return PackageResponse
     */
    public PackageResponse createPackage(PackageRequest packageRequest) {
        if (packageRequest == null || packageRequest.getPackageName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package name is required");
        }
        
        Package package_ = new Package();
        package_.setPackageId(UUID.randomUUID().toString());
        package_.setPackageName(packageRequest.getPackageName());
        package_.setDescription(packageRequest.getDescription());
        package_.setDurationMonths(packageRequest.getDurationMonths());
        package_.setPrice(packageRequest.getPrice());
        package_.setBenefits(packageRequest.getBenefits());
        package_.setIsActive(packageRequest.getIsActive() != null ? packageRequest.getIsActive() : true);
        
        Package savedPackage = packageRepository.save(package_);
        return mapToResponse(savedPackage);
    }
    
    /**
     * Get all active packages
     * @return List of PackageResponse
     */
    public List<PackageResponse> getAllActivePackages() {
        return packageRepository.findActivePackages()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all packages
     * @return List of PackageResponse
     */
    public List<PackageResponse> getAllPackages() {
        return packageRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get package by ID
     * @param packageId Package ID
     * @return PackageResponse
     */
    public PackageResponse getPackageById(String packageId) {
        Package package_ = packageRepository.findById(packageId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));
        return mapToResponse(package_);
    }
    
    /**
     * Update package
     * @param packageId Package ID
     * @param packageRequest Updated package details
     * @return PackageResponse
     */
    public PackageResponse updatePackage(String packageId, PackageRequest packageRequest) {
        Package package_ = packageRepository.findById(packageId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));
        
        if (packageRequest.getPackageName() != null) {
            package_.setPackageName(packageRequest.getPackageName());
        }
        if (packageRequest.getDescription() != null) {
            package_.setDescription(packageRequest.getDescription());
        }
        if (packageRequest.getPrice() != null) {
            package_.setPrice(packageRequest.getPrice());
        }
        if (packageRequest.getDurationMonths() != null) {
            package_.setDurationMonths(packageRequest.getDurationMonths());
        }
        if (packageRequest.getBenefits() != null) {
            package_.setBenefits(packageRequest.getBenefits());
        }
        if (packageRequest.getIsActive() != null) {
            package_.setIsActive(packageRequest.getIsActive());
        }
        
        Package updatedPackage = packageRepository.save(package_);
        return mapToResponse(updatedPackage);
    }
    
    /**
     * Delete package
     * @param packageId Package ID
     */
    public void deletePackage(String packageId) {
        if (!packageRepository.existsById(packageId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found");
        }
        packageRepository.deleteById(packageId);
    }
    
    /**
     * Map Package entity to PackageResponse DTO
     * @param package_ Package entity
     * @return PackageResponse
     */
    private PackageResponse mapToResponse(Package package_) {
        return PackageResponse.builder()
            .packageId(package_.getPackageId())
            .packageName(package_.getPackageName())
            .description(package_.getDescription())
            .durationMonths(package_.getDurationMonths())
            .price(package_.getPrice())
            .benefits(package_.getBenefits())
            .isActive(package_.getIsActive())
            .createdAt(package_.getCreatedAt())
            .updatedAt(package_.getUpdatedAt())
            .build();
    }
}
