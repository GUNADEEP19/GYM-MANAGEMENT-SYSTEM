package com.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    private String memberId;
    
    private String packageId;
    
    private Double amount;
    
    private String paymentMethod; // CREDIT_CARD, UPI
}
