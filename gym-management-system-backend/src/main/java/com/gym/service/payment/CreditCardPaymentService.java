package com.gym.service.payment;

import com.gym.model.Payment;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Credit Card Payment Strategy Implementation
 */
@Component
public class CreditCardPaymentService implements PaymentService {
    
    @Override
    public boolean processPayment(Payment payment) {
        if (!validatePayment(payment)) {
            return false;
        }
        
        // Simulate credit card payment processing
        try {
            // Simulate API call to payment gateway (e.g., Stripe, PayPal)
            boolean success = simulateCreditCardTransaction(payment);
            
            if (success) {
                String transactionId = "CC-" + UUID.randomUUID().toString().substring(0, 8);
                payment.setTransactionId(transactionId);
                return true;
            }
            
            payment.setFailureReason("Credit card declined");
            return false;
            
        } catch (Exception e) {
            payment.setFailureReason("Credit card processing error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "CREDIT_CARD";
    }
    
    @Override
    public boolean validatePayment(Payment payment) {
        // Validate credit card details
        if (payment == null || payment.getAmount() == null || payment.getAmount() <= 0) {
            return false;
        }
        
        // In real scenario, would validate card number, CVV, expiry, etc.
        return true;
    }
    
    /**
     * Simulate credit card transaction
     * @param payment Payment object
     * @return true if transaction successful
     */
    private boolean simulateCreditCardTransaction(Payment payment) {
        // Simulate: 80% success rate
        return Math.random() > 0.2;
    }
}
