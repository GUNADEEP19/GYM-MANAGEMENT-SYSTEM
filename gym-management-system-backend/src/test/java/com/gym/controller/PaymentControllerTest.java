package com.gym.controller;

import com.gym.dto.PaymentRequest;
import com.gym.dto.PaymentResponse;
import com.gym.dto.PaymentStatus;
import com.gym.model.Member;
import com.gym.model.Package;
import com.gym.model.Payment;
import com.gym.repository.MemberRepository;
import com.gym.repository.PackageRepository;
import com.gym.repository.PaymentRepository;
import com.gym.service.payment.PaymentManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("Payment Controller Tests")
class PaymentControllerTest {
    
    @Mock
    private PaymentManager paymentManager;
    
    @Mock
    private PaymentRepository paymentRepository;
    
    @Mock
    private MemberRepository memberRepository;
    
    @Mock
    private PackageRepository packageRepository;
    
    @InjectMocks
    private PaymentController paymentController;
    
    private Member testMember;
    private Package testPackage;
    private Payment testPayment;
    private PaymentRequest paymentRequest;
    
    @BeforeEach
    void setUp() {
        
        // Setup test member
        testMember = new Member();
        testMember.setUserId("member-123");
        testMember.setName("Test Member");
        testMember.setEmail("member@test.com");
        testMember.setPhone("1234567890");
        
        // Setup test package
        testPackage = new Package();
        testPackage.setPackageId("pkg-123");
        testPackage.setPackageName("Premium Package");
        testPackage.setPrice(2999.0);
        testPackage.setDurationMonths(3);
        
        // Setup test payment
        testPayment = new Payment();
        testPayment.setPaymentId("pay-123");
        testPayment.setMember(testMember);
        testPayment.setPackage_(testPackage);
        testPayment.setAmount(2999.0);
        testPayment.setPaymentMethod("CREDIT_CARD");
        testPayment.setStatus(PaymentStatus.SUCCESS);
        testPayment.setTransactionId("TXN-123");
        
        // Setup payment request
        paymentRequest = new PaymentRequest();
        paymentRequest.setMemberId("member-123");
        paymentRequest.setPackageId("pkg-123");
        paymentRequest.setAmount(2999.0);
        paymentRequest.setPaymentMethod("CREDIT_CARD");
    }
    
    @Test
    @DisplayName("Should process payment successfully")
    void testProcessPaymentSuccess() {
        // Arrange
        when(memberRepository.findById("member-123")).thenReturn(Optional.of(testMember));
        when(packageRepository.findById("pkg-123")).thenReturn(Optional.of(testPackage));
        when(paymentManager.processPayment(any(Payment.class))).thenReturn(testPayment);
        
        // Act
        ResponseEntity<PaymentResponse> response = paymentController.processPayment(paymentRequest);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("pay-123", response.getBody().getPaymentId());
        assertEquals(PaymentStatus.SUCCESS, response.getBody().getStatus());
        verify(memberRepository, times(1)).findById("member-123");
        verify(packageRepository, times(1)).findById("pkg-123");
        verify(paymentManager, times(1)).processPayment(any(Payment.class));
    }
    
    @Test
    @DisplayName("Should fail payment when member not found")
    void testProcessPaymentMemberNotFound() {
        // Arrange
        when(memberRepository.findById("member-123")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            paymentController.processPayment(paymentRequest);
        });
    }
    
    @Test
    @DisplayName("Should fail payment when package not found")
    void testProcessPaymentPackageNotFound() {
        // Arrange
        when(memberRepository.findById("member-123")).thenReturn(Optional.of(testMember));
        when(packageRepository.findById("pkg-123")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            paymentController.processPayment(paymentRequest);
        });
    }
    
    @Test
    @DisplayName("Should reject null payment request")
    void testProcessPaymentNullRequest() {
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            paymentController.processPayment(null);
        });
    }
    
    @Test
    @DisplayName("Should reject payment request with missing member ID")
    void testProcessPaymentMissingMemberId() {
        // Arrange
        PaymentRequest invalidRequest = new PaymentRequest();
        invalidRequest.setMemberId(null);
        invalidRequest.setPackageId("pkg-123");
        invalidRequest.setPaymentMethod("CREDIT_CARD");
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            paymentController.processPayment(invalidRequest);
        });
    }
    
    @Test
    @DisplayName("Should reject payment request with missing payment method")
    void testProcessPaymentMissingPaymentMethod() {
        // Arrange
        PaymentRequest invalidRequest = new PaymentRequest();
        invalidRequest.setMemberId("member-123");
        invalidRequest.setPackageId("pkg-123");
        invalidRequest.setPaymentMethod(null);
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            paymentController.processPayment(invalidRequest);
        });
    }
    
    @Test
    @DisplayName("Should get payment status successfully")
    void testGetPaymentStatusSuccess() {
        // Arrange
        when(paymentRepository.findById("pay-123")).thenReturn(Optional.of(testPayment));
        
        // Act
        ResponseEntity<PaymentResponse> response = paymentController.getPaymentStatus("pay-123");
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("pay-123", response.getBody().getPaymentId());
        assertEquals(PaymentStatus.SUCCESS, response.getBody().getStatus());
        verify(paymentRepository, times(1)).findById("pay-123");
    }
    
    @Test
    @DisplayName("Should fail when payment not found")
    void testGetPaymentStatusNotFound() {
        // Arrange
        when(paymentRepository.findById("pay-invalid")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            paymentController.getPaymentStatus("pay-invalid");
        });
    }
    
    @Test
    @DisplayName("Should handle payment failure status")
    void testGetFailedPaymentStatus() {
        // Arrange
        Payment failedPayment = new Payment();
        failedPayment.setPaymentId("pay-fail");
        failedPayment.setStatus(PaymentStatus.FAILED);
        failedPayment.setFailureReason("Card declined");
        failedPayment.setMember(testMember);
        failedPayment.setPackage_(testPackage);
        failedPayment.setAmount(2999.0);
        failedPayment.setPaymentMethod("CREDIT_CARD");
        
        when(paymentRepository.findById("pay-fail")).thenReturn(Optional.of(failedPayment));
        
        // Act
        ResponseEntity<PaymentResponse> response = paymentController.getPaymentStatus("pay-fail");
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PaymentStatus.FAILED, response.getBody().getStatus());
        assertEquals("Card declined", response.getBody().getFailureReason());
    }
}
