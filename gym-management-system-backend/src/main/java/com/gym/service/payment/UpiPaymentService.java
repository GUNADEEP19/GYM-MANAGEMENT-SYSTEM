package com.gym.service.payment;

import com.gym.model.Payment;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * UPI Payment Strategy Implementation
 */
@Component
public class UpiPaymentService implements PaymentService {
    
    @Override
    public boolean processPayment(Payment payment) {
        if (!validatePayment(payment)) {
            return false;
        }
        
        // Simulate UPI payment processing
        try {
            // Simulate API call to UPI gateway (e.g., NPCI, Payment aggregators)
            boolean success = simulateUpiTransaction(payment);
            
            if (success) {
                String transactionId = "UPI-" + UUID.randomUUID().toString().substring(0, 8);
                payment.setTransactionId(transactionId);
                return true;
            }
            
            payment.setFailureReason("UPI transaction rejected");
            return false;
            
        } catch (Exception e) {
            payment.setFailureReason("UPI processing error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "UPI";
    }
    
    @Override
    public boolean validatePayment(Payment payment) {
        // Validate UPI payment details
        if (payment == null || payment.getAmount() == null || payment.getAmount() <= 0) {
            return false;
        }
        
        // In real scenario, would validate UPI ID, account, etc.
        return true;
    }
    
    /**
     * Simulate UPI transaction
     * @param payment Payment object
     * @return true if transaction successful
     */
    private boolean simulateUpiTransaction(Payment payment) {
        // Simulate: 90% success rate for UPI (typically more reliable)
        return Math.random() > 0.1;
    }
}
