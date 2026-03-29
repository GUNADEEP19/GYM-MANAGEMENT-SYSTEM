package com.gym.controller;

import com.gym.dto.PackageRequest;
import com.gym.dto.PackageResponse;
import com.gym.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Package Management
 */
@RestController
@RequestMapping("/api/package")
public class PackageController {
    
    private final PackageService packageService;
    
    @Autowired
    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }
    
    /**
     * Create new package
     * POST /api/package/create
     * @param packageRequest Package details
     * @return PackageResponse
     */
    @PostMapping("/create")
    public ResponseEntity<PackageResponse> createPackage(@RequestBody PackageRequest packageRequest) {
        PackageResponse response = packageService.createPackage(packageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get all active packages
     * GET /api/package/active
     * @return List of active packages
     */
    @GetMapping("/active")
    public ResponseEntity<List<PackageResponse>> getAllActivePackages() {
        List<PackageResponse> response = packageService.getAllActivePackages();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all packages
     * GET /api/package/all
     * @return List of all packages
     */
    @GetMapping("/all")
    public ResponseEntity<List<PackageResponse>> getAllPackages() {
        List<PackageResponse> response = packageService.getAllPackages();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get package by ID
     * GET /api/package/{id}
     * @param packageId Package ID
     * @return PackageResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> getPackageById(@PathVariable("id") String packageId) {
        PackageResponse response = packageService.getPackageById(packageId);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update package
     * PUT /api/package/{id}
     * @param packageId Package ID
     * @param packageRequest Updated package details
     * @return PackageResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<PackageResponse> updatePackage(
        @PathVariable("id") String packageId,
        @RequestBody PackageRequest packageRequest) {
        PackageResponse response = packageService.updatePackage(packageId, packageRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete package
     * DELETE /api/package/{id}
     * @param packageId Package ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePackage(@PathVariable("id") String packageId) {
        packageService.deletePackage(packageId);
        return ResponseEntity.ok("Package deleted successfully");
    }
}
