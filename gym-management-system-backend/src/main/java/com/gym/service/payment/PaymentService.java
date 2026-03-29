package com.gym.service.payment;

import com.gym.model.Payment;
import com.gym.dto.PaymentStatus;

/**
 * Strategy Pattern Interface for Payment Processing
 * Defines the contract for different payment methods
 */
public interface PaymentService {
    
    /**
     * Process payment transaction
     * @param payment Payment object containing payment details
     * @return true if payment successful, false otherwise
     */
    boolean processPayment(Payment payment);
    
    /**
     * Get the payment method type
     * @return payment method name
     */
    String getPaymentMethod();
    
    /**
     * Validate payment details
     * @param payment Payment object to validate
     * @return true if details are valid, false otherwise
     */
    boolean validatePayment(Payment payment);
}
