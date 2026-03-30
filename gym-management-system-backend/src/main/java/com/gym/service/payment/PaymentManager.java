package com.gym.service.payment;

import com.gym.model.Payment;
import com.gym.dto.PaymentStatus;
import com.gym.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

/**
 * Payment Manager - Orchestrates payment operations
 * Handles payment processing, receipt generation, and failure handling
 */
@Slf4j
@Service
public class PaymentManager {
    
    private final PaymentFactory paymentFactory;
    private final PaymentRepository paymentRepository;
    
    @Autowired
    public PaymentManager(PaymentFactory paymentFactory, PaymentRepository paymentRepository) {
        this.paymentFactory = paymentFactory;
        this.paymentRepository = paymentRepository;
    }
    
    /**
     * Process payment using appropriate payment method
     * @param payment Payment object with transaction details
     * @return Payment object with updated status
     */
    public Payment processPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        
        // Set initial state
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        
        // Save initial payment record
        log.info("Initiating {} payment for Member {} - Amount: {}", payment.getPaymentMethod(), payment.getMember().getUserId(), payment.getAmount());
        payment = paymentRepository.save(payment);
        
        try {
            // Get appropriate payment service based on method
            PaymentService paymentService = paymentFactory.getPaymentService(payment.getPaymentMethod());
            
            // Process the payment
            boolean success = paymentService.processPayment(payment);
            
            // Update payment status
            if (success) {
                log.info("Payment SUCCESS for Member {}", payment.getMember().getUserId());
                payment.setStatus(PaymentStatus.SUCCESS);
                generateReceipt(payment);
            } else {
                log.warn("Payment FAILED for Member {} utilizing {}", payment.getMember().getUserId(), payment.getPaymentMethod());
                payment.setStatus(PaymentStatus.FAILED);
                handleFailure(payment);
            }
            
        } catch (IllegalArgumentException e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Invalid payment method: " + e.getMessage());
            handleFailure(payment);
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Payment processing error: " + e.getMessage());
            handleFailure(payment);
        }
        
        // Save updated payment record
        return paymentRepository.save(payment);
    }
    
    /**
     * Generate receipt after successful payment
     * @param payment Payment object
     */
    public void generateReceipt(Payment payment) {
        if (payment == null || payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException("Can only generate receipt for successful payments");
        }
        
        try {
            String receiptId = "RECEIPT-" + UUID.randomUUID().toString().substring(0, 10);
            String receiptContent = buildReceiptContent(payment, receiptId);
            
            // In real scenario: save receipt to file system or cloud storage
            String receiptUrl = "/receipts/" + receiptId + ".pdf";
            payment.setReceiptUrl(receiptUrl);
            
            // Log receipt generation
            log.info("Receipt generated: {}", receiptUrl);
            log.debug("\n{}", receiptContent);
            
        } catch (Exception e) {
            log.error("Error generating receipt: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Handle payment failure
     * @param payment Payment object with failure details
     */
    public void handleFailure(Payment payment) {
        if (payment == null || payment.getStatus() != PaymentStatus.FAILED) {
            throw new IllegalArgumentException("Can only handle failed payments");
        }
        
        // Log failure
        log.error("Payment failed for Member: {}, Amount: {}, Reason: {}", 
            payment.getMember().getName(), 
            payment.getAmount(), 
            payment.getFailureReason()
        );
        
        // In real scenario: 
        // - Send email to member
        // - Trigger retry mechanism
        // - Create support ticket if needed
    }
    
    /**
     * Get payment status
     * @param paymentId Payment ID
     * @return Payment status or null if not found
     */
    public PaymentStatus getPaymentStatus(String paymentId) {
        return paymentRepository.findById(paymentId)
            .map(Payment::getStatus)
            .orElse(null);
    }
    
    /**
     * Build receipt content
     * @param payment Payment object
     * @param receiptId Receipt ID
     * @return Receipt content as string
     */
    private String buildReceiptContent(Payment payment, String receiptId) {
        return String.format(
            "========== PAYMENT RECEIPT ==========\n" +
            "Receipt ID: %s\n" +
            "Payment ID: %s\n" +
            "Member: %s\n" +
            "Package: %s\n" +
            "Amount: Rs. %.2f\n" +
            "Payment Method: %s\n" +
            "Transaction ID: %s\n" +
            "Date: %s\n" +
            "Status: %s\n" +
            "====================================\n",
            receiptId,
            payment.getPaymentId(),
            payment.getMember().getName(),
            payment.getPackage_().getPackageName(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getTransactionId(),
            payment.getPaymentDate(),
            payment.getStatus()
        );
    }
}
