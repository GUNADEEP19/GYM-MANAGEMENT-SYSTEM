package com.gym.service.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Payment Factory Tests")
public class PaymentFactoryTest {
    
    private PaymentFactory paymentFactory;
    private CreditCardPaymentService creditCardPaymentService;
    private UpiPaymentService upiPaymentService;
    
    @BeforeEach
    void setUp() {
        creditCardPaymentService = new CreditCardPaymentService();
        upiPaymentService = new UpiPaymentService();
        paymentFactory = new PaymentFactory(creditCardPaymentService, upiPaymentService);
    }
    
    @Test
    @DisplayName("Should return CreditCardPaymentService for CREDIT_CARD method")
    void testGetCreditCardPaymentService() {
        // Act
        PaymentService service = paymentFactory.getPaymentService("CREDIT_CARD");
        
        // Assert
        assertNotNull(service);
        assertInstanceOf(CreditCardPaymentService.class, service);
    }
    
    @Test
    @DisplayName("Should return UpiPaymentService for UPI method")
    void testGetUpiPaymentService() {
        // Act
        PaymentService service = paymentFactory.getPaymentService("UPI");
        
        // Assert
        assertNotNull(service);
        assertInstanceOf(UpiPaymentService.class, service);
    }
    
    @Test
    @DisplayName("Should handle case-insensitive payment method")
    void testGetPaymentServiceCaseInsensitive() {
        // Act
        PaymentService service1 = paymentFactory.getPaymentService("credit_card");
        PaymentService service2 = paymentFactory.getPaymentService("upi");
        
        // Assert
        assertInstanceOf(CreditCardPaymentService.class, service1);
        assertInstanceOf(UpiPaymentService.class, service2);
    }
    
    @Test
    @DisplayName("Should throw exception for unsupported payment method")
    void testGetPaymentServiceUnsupported() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            paymentFactory.getPaymentService("BITCOIN");
        });
    }
    
    @Test
    @DisplayName("Should throw exception for null payment method")
    void testGetPaymentServiceNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            paymentFactory.getPaymentService(null);
        });
    }
    
    @Test
    @DisplayName("Should throw exception for empty payment method")
    void testGetPaymentServiceEmpty() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            paymentFactory.getPaymentService("");
        });
    }
}
