package com.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    
    private String paymentId;
    
    private String memberId;
    
    private String packageId;
    
    private Double amount;
    
    private String paymentMethod;
    
    private PaymentStatus status;
    
    private String transactionId;
    
    private String receiptUrl;
    
    private LocalDateTime paymentDate;
    
    private String failureReason;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
