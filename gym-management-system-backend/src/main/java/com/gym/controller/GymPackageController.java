package com.gym.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.ApiResponse;
import com.gym.dto.CreatePackageRequest;
import com.gym.dto.GymPackageResponse;
import com.gym.model.GymPackage;
import com.gym.service.GymPackageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/packages")
@Validated
public class GymPackageController {

    private final GymPackageService packageService;

    public GymPackageController(GymPackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<GymPackageResponse> create(@Valid @RequestBody CreatePackageRequest request) {
        GymPackage created = packageService.create(request);
        return ApiResponse.ok(new GymPackageResponse(created.getId(), created.getName(), created.getDurationMonths(), created.getPrice(), null));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TRAINER','MEMBER')")
    public ApiResponse<List<GymPackageResponse>> list(@RequestParam(defaultValue = "false") boolean activeOnly) {
        List<GymPackage> list = activeOnly ? packageService.listActive() : packageService.listAll();
        List<GymPackageResponse> resp = list.stream()
                .map(p -> new GymPackageResponse(p.getId(), p.getName(), p.getDurationMonths(), p.getPrice(), null))
                .toList();
        return ApiResponse.ok(resp);
    }
}
