package com.gym.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.CreatePackageRequest;
import com.gym.model.GymPackage;
import com.gym.repository.GymPackageRepository;

@Service
public class GymPackageService {

    private final GymPackageRepository packageRepository;

    public GymPackageService(GymPackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public GymPackage create(CreatePackageRequest request) {
        GymPackage gymPackage = new GymPackage();
        gymPackage.setName(request.name());
        gymPackage.setDurationMonths(request.durationMonths());
        gymPackage.setPrice(request.price());
        gymPackage.setActive(true);
        return packageRepository.save(gymPackage);
    }

    public List<GymPackage> listAll() {
        return packageRepository.findAll();
    }

    public List<GymPackage> listActive() {
        return packageRepository.findByActiveTrue();
    }

    public GymPackage getById(Long id) {
        Objects.requireNonNull(id, "id");
        return packageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));
    }
}
