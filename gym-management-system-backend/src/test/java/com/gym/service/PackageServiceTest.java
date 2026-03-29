package com.gym.service;

import com.gym.dto.PackageRequest;
import com.gym.dto.PackageResponse;
import com.gym.model.Package;
import com.gym.repository.PackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Package Service Tests")
public class PackageServiceTest {
    
    @Mock
    private PackageRepository packageRepository;
    
    @InjectMocks
    private PackageService packageService;
    
    private PackageRequest packageRequest;
    private Package testPackage;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        packageRequest = new PackageRequest();
        packageRequest.setPackageName("Premium Package");
        packageRequest.setDescription("Premium membership");
        packageRequest.setPrice(2999.0);
        packageRequest.setDurationMonths(3);
        packageRequest.setBenefits("Gym access, Personal training");
        packageRequest.setIsActive(true);
        
        testPackage = new Package();
        testPackage.setPackageId("pkg-1");
        testPackage.setPackageName("Premium Package");
        testPackage.setDescription("Premium membership");
        testPackage.setPrice(2999.0);
        testPackage.setDurationMonths(3);
        testPackage.setBenefits("Gym access, Personal training");
        testPackage.setIsActive(true);
    }
    
    @Test
    @DisplayName("Should create package successfully")
    void testCreatePackageSuccess() {
        // Arrange
        when(packageRepository.save(any(Package.class))).thenReturn(testPackage);
        
        // Act
        PackageResponse response = packageService.createPackage(packageRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("Premium Package", response.getPackageName());
        assertEquals(2999.0, response.getPrice());
        verify(packageRepository, times(1)).save(any(Package.class));
    }
    
    @Test
    @DisplayName("Should fail to create package with null name")
    void testCreatePackageNullName() {
        // Arrange
        packageRequest.setPackageName(null);
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            packageService.createPackage(packageRequest);
        });
    }
    
    @Test
    @DisplayName("Should fail to create package with null request")
    void testCreatePackageNullRequest() {
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            packageService.createPackage(null);
        });
    }
    
    @Test
    @DisplayName("Should get all active packages")
    void testGetAllActivePackages() {
        // Arrange
        List<Package> activePackages = Arrays.asList(testPackage);
        when(packageRepository.findActivePackages()).thenReturn(activePackages);
        
        // Act
        List<PackageResponse> response = packageService.getAllActivePackages();
        
        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Premium Package", response.get(0).getPackageName());
        verify(packageRepository, times(1)).findActivePackages();
    }
    
    @Test
    @DisplayName("Should get all packages")
    void testGetAllPackages() {
        // Arrange
        List<Package> packages = Arrays.asList(testPackage);
        when(packageRepository.findAll()).thenReturn(packages);
        
        // Act
        List<PackageResponse> response = packageService.getAllPackages();
        
        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(packageRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("Should get package by ID")
    void testGetPackageById() {
        // Arrange
        when(packageRepository.findById("pkg-1")).thenReturn(Optional.of(testPackage));
        
        // Act
        PackageResponse response = packageService.getPackageById("pkg-1");
        
        // Assert
        assertNotNull(response);
        assertEquals("pkg-1", response.getPackageId());
        assertEquals("Premium Package", response.getPackageName());
        verify(packageRepository, times(1)).findById("pkg-1");
    }
    
    @Test
    @DisplayName("Should fail to get non-existent package")
    void testGetPackageByIdNotFound() {
        // Arrange
        when(packageRepository.findById("pkg-invalid")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            packageService.getPackageById("pkg-invalid");
        });
    }
    
    @Test
    @DisplayName("Should update package successfully")
    void testUpdatePackageSuccess() {
        // Arrange
        Package updatePackage = new Package();
        updatePackage.setPackageId("pkg-1");
        updatePackage.setPackageName("Premium Package");
        updatePackage.setDescription("Premium membership");
        updatePackage.setPrice(3999.0);
        updatePackage.setDurationMonths(3);
        updatePackage.setBenefits("Gym access, Personal training");
        updatePackage.setIsActive(true);
        
        when(packageRepository.findById("pkg-1")).thenReturn(Optional.of(testPackage));
        when(packageRepository.save(any(Package.class))).thenReturn(updatePackage);
        
        PackageRequest updateRequest = new PackageRequest();
        updateRequest.setPrice(3999.0);
        
        // Act
        PackageResponse response = packageService.updatePackage("pkg-1", updateRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(3999.0, response.getPrice());
        verify(packageRepository, times(1)).findById("pkg-1");
        verify(packageRepository, times(1)).save(any(Package.class));
    }
    
    @Test
    @DisplayName("Should delete package successfully")
    void testDeletePackageSuccess() {
        // Arrange
        when(packageRepository.existsById("pkg-1")).thenReturn(true);
        
        // Act
        assertDoesNotThrow(() -> {
            packageService.deletePackage("pkg-1");
        });
        
        // Assert
        verify(packageRepository, times(1)).deleteById("pkg-1");
    }
    
    @Test
    @DisplayName("Should fail to delete non-existent package")
    void testDeletePackageNotFound() {
        // Arrange
        when(packageRepository.existsById("pkg-invalid")).thenReturn(false);
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            packageService.deletePackage("pkg-invalid");
        });
    }
}
