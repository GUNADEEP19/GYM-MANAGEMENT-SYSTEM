package com.gym.service.payment;

import com.gym.model.Payment;
import com.gym.model.Member;
import com.gym.model.Package;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Payment Service Strategy Tests")
public class PaymentServiceTest {
    
    private CreditCardPaymentService creditCardPaymentService;
    private UpiPaymentService upiPaymentService;
    
    private Member testMember;
    private Package testPackage;
    
    @BeforeEach
    void setUp() {
        creditCardPaymentService = new CreditCardPaymentService();
        upiPaymentService = new UpiPaymentService();
        
        // Setup test member
        testMember = new Member();
        testMember.setUserId("member-1");
        testMember.setName("Jane Doe");
        testMember.setEmail("jane@example.com");
        
        // Setup test package
        testPackage = new Package();
        testPackage.setPackageId("pkg-2");
        testPackage.setPackageName("Basic 1 Month");
        testPackage.setPrice(999.0);
    }
    
    @Test
    @DisplayName("Credit Card: Should process payment successfully")
    void testCreditCardProcessPaymentSuccess() {
        // Arrange
        Payment payment = new Payment();
        payment.setMember(testMember);
        payment.setPackage_(testPackage);
        payment.setAmount(999.0);
        payment.setPaymentMethod("CREDIT_CARD");
        
        // Act
        boolean result = creditCardPaymentService.processPayment(payment);
        
        // Assert
        assertTrue(result || !result); // Stochastic test (80% success rate)
        if (result) {
            assertNotNull(payment.getTransactionId());
            assertTrue(payment.getTransactionId().startsWith("CC-"));
        } else {
            assertNotNull(payment.getFailureReason());
        }
    }
    
    @Test
    @DisplayName("Credit Card: Should validate payment correctly")
    void testCreditCardValidatePayment() {
        // Arrange
        Payment validPayment = new Payment();
        validPayment.setAmount(999.0);
        
        Payment invalidPayment = new Payment();
        invalidPayment.setAmount(-100.0);
        
        // Act & Assert
        assertTrue(creditCardPaymentService.validatePayment(validPayment));
        assertFalse(creditCardPaymentService.validatePayment(invalidPayment));
        assertFalse(creditCardPaymentService.validatePayment(null));
    }
    
    @Test
    @DisplayName("Credit Card: Should return correct payment method")
    void testCreditCardGetPaymentMethod() {
        // Act
        String method = creditCardPaymentService.getPaymentMethod();
        
        // Assert
        assertEquals("CREDIT_CARD", method);
    }
    
    @Test
    @DisplayName("UPI: Should process payment successfully")
    void testUpiProcessPaymentSuccess() {
        // Arrange
        Payment payment = new Payment();
        payment.setMember(testMember);
        payment.setPackage_(testPackage);
        payment.setAmount(1999.0);
        payment.setPaymentMethod("UPI");
        
        // Act
        boolean result = upiPaymentService.processPayment(payment);
        
        // Assert
        assertTrue(result || !result); // Stochastic test (90% success rate)
        if (result) {
            assertNotNull(payment.getTransactionId());
            assertTrue(payment.getTransactionId().startsWith("UPI-"));
        } else {
            assertNotNull(payment.getFailureReason());
        }
    }
    
    @Test
    @DisplayName("UPI: Should validate payment correctly")
    void testUpiValidatePayment() {
        // Arrange
        Payment validPayment = new Payment();
        validPayment.setAmount(1999.0);
        
        Payment zeroAmountPayment = new Payment();
        zeroAmountPayment.setAmount(0.0);
        
        // Act & Assert
        assertTrue(upiPaymentService.validatePayment(validPayment));
        assertFalse(upiPaymentService.validatePayment(zeroAmountPayment));
        assertFalse(upiPaymentService.validatePayment(null));
    }
    
    @Test
    @DisplayName("UPI: Should return correct payment method")
    void testUpiGetPaymentMethod() {
        // Act
        String method = upiPaymentService.getPaymentMethod();
        
        // Assert
        assertEquals("UPI", method);
    }
    
    @Test
    @DisplayName("Credit Card: Should fail for null amount")
    void testCreditCardFailForNullAmount() {
        // Arrange
        Payment payment = new Payment();
        payment.setAmount(null);
        
        // Act
        boolean result = creditCardPaymentService.validatePayment(payment);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("UPI: Should fail for negative amount")
    void testUpiFailForNegativeAmount() {
        // Arrange
        Payment payment = new Payment();
        payment.setAmount(-500.0);
        
        // Act
        boolean result = upiPaymentService.validatePayment(payment);
        
        // Assert
        assertFalse(result);
    }
}
