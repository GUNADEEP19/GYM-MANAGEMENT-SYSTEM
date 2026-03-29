package com.gym.service.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory Pattern Implementation for Payment Services
 * Responsible for creating and returning appropriate payment service
 */
@Component
public class PaymentFactory {
    
    private final CreditCardPaymentService creditCardPaymentService;
    private final UpiPaymentService upiPaymentService;
    
    @Autowired
    public PaymentFactory(CreditCardPaymentService creditCardPaymentService,
                         UpiPaymentService upiPaymentService) {
        this.creditCardPaymentService = creditCardPaymentService;
        this.upiPaymentService = upiPaymentService;
    }
    
    /**
     * Get payment service based on payment method
     * @param paymentMethod Payment method type (CREDIT_CARD, UPI)
     * @return PaymentService implementation
     * @throws IllegalArgumentException if payment method is not supported
     */
    public PaymentService getPaymentService(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be null or empty");
        }
        
        return switch (paymentMethod.toUpperCase()) {
            case "CREDIT_CARD" -> creditCardPaymentService;
            case "UPI" -> upiPaymentService;
            default -> throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        };
    }
}
