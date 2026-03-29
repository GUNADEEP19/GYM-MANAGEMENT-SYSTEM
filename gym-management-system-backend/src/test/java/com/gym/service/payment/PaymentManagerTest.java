package com.gym.service.payment;

import com.gym.model.Payment;
import com.gym.model.Member;
import com.gym.model.Package;
import com.gym.dto.PaymentStatus;
import com.gym.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Payment Manager Tests")
public class PaymentManagerTest {
    
    @Mock
    private PaymentFactory paymentFactory;
    
    @Mock
    private PaymentRepository paymentRepository;
    
    @Mock
    private PaymentService paymentService;
    
    @InjectMocks
    private PaymentManager paymentManager;
    
    private Member testMember;
    private Package testPackage;
    private Payment testPayment;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test member
        testMember = new Member();
        testMember.setUserId("test-member-1");
        testMember.setName("John Doe");
        testMember.setEmail("john@example.com");
        testMember.setPhone("9876543210");
        
        // Setup test package
        testPackage = new Package();
        testPackage.setPackageId("pkg-1");
        testPackage.setPackageName("Premium 3 Months");
        testPackage.setPrice(2999.0);
        testPackage.setDurationMonths(3);
        
        // Setup test payment
        testPayment = new Payment();
        testPayment.setMember(testMember);
        testPayment.setPackage_(testPackage);
        testPayment.setAmount(2999.0);
        testPayment.setPaymentMethod("CREDIT_CARD");
    }
    
    @Test
    @DisplayName("Should process payment successfully")
    void testProcessPaymentSuccess() {
        // Arrange
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(paymentFactory.getPaymentService("CREDIT_CARD")).thenReturn(paymentService);
        when(paymentService.processPayment(any(Payment.class))).thenReturn(true);
        
        // Mock the payment after update
        Payment updatedPayment = new Payment();
        updatedPayment.setPaymentId("pay-123");
        updatedPayment.setStatus(PaymentStatus.SUCCESS);
        updatedPayment.setTransactionId("TXN-123");
        updatedPayment.setMember(testMember);
        updatedPayment.setPackage_(testPackage);
        updatedPayment.setAmount(testPayment.getAmount());
        updatedPayment.setPaymentMethod("CREDIT_CARD");
        
        when(paymentRepository.save(argThat(p -> p.getStatus() == PaymentStatus.SUCCESS)))
            .thenReturn(updatedPayment);
        
        // Act
        Payment result = paymentManager.processPayment(testPayment);
        
        // Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getTransactionId());
        verify(paymentFactory, times(1)).getPaymentService("CREDIT_CARD");
        verify(paymentService, times(1)).processPayment(any());
    }
    
    @Test
    @DisplayName("Should handle payment failure")
    void testProcessPaymentFailure() {
        // Arrange
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(paymentFactory.getPaymentService("CREDIT_CARD")).thenReturn(paymentService);
        when(paymentService.processPayment(any(Payment.class))).thenReturn(false);
        
        // Mock the payment after update
        Payment failedPayment = new Payment();
        failedPayment.setPaymentId("pay-456");
        failedPayment.setStatus(PaymentStatus.FAILED);
        failedPayment.setFailureReason("Credit card declined");
        failedPayment.setMember(testMember);
        failedPayment.setPackage_(testPackage);
        failedPayment.setAmount(testPayment.getAmount());
        failedPayment.setPaymentMethod("CREDIT_CARD");
        
        when(paymentRepository.save(argThat(p -> p.getStatus() == PaymentStatus.FAILED)))
            .thenReturn(failedPayment);
        
        // Act
        Payment result = paymentManager.processPayment(testPayment);
        
        // Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.FAILED, result.getStatus());
        assertNotNull(result.getFailureReason());
        verify(paymentFactory, times(1)).getPaymentService("CREDIT_CARD");
    }
    
    @Test
    @DisplayName("Should throw exception for null payment")
    void testProcessPaymentWithNullPayment() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            paymentManager.processPayment(null);
        });
    }
    
    @Test
    @DisplayName("Should generate receipt for successful payment")
    void testGenerateReceiptSuccess() {
        // Arrange
        testPayment.setPaymentId("pay-789");
        testPayment.setStatus(PaymentStatus.SUCCESS);
        testPayment.setTransactionId("TXN-success");
        
        // Act
        assertDoesNotThrow(() -> {
            paymentManager.generateReceipt(testPayment);
        });
        
        // Assert
        assertNotNull(testPayment.getReceiptUrl());
        assertTrue(testPayment.getReceiptUrl().contains("RECEIPT-"));
    }
    
    @Test
    @DisplayName("Should throw exception when generating receipt for failed payment")
    void testGenerateReceiptForFailedPayment() {
        // Arrange
        testPayment.setPaymentId("pay-fail");
        testPayment.setStatus(PaymentStatus.FAILED);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            paymentManager.generateReceipt(testPayment);
        });
    }
    
    @Test
    @DisplayName("Should handle payment failure correctly")
    void testHandleFailure() {
        // Arrange
        testPayment.setPaymentId("pay-handle-fail");
        testPayment.setStatus(PaymentStatus.FAILED);
        testPayment.setFailureReason("Insufficient funds");
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            paymentManager.handleFailure(testPayment);
        });
    }
    
    @Test
    @DisplayName("Should get payment status")
    void testGetPaymentStatus() {
        // Arrange
        Payment existingPayment = new Payment();
        existingPayment.setPaymentId("pay-status");
        existingPayment.setStatus(PaymentStatus.SUCCESS);
        
        when(paymentRepository.findById("pay-status")).thenReturn(java.util.Optional.of(existingPayment));
        
        // Act
        PaymentStatus status = paymentManager.getPaymentStatus("pay-status");
        
        // Assert
        assertEquals(PaymentStatus.SUCCESS, status);
    }
}
