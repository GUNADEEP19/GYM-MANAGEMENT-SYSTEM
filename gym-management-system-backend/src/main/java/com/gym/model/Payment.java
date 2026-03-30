package com.gym.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.gym.dto.PaymentStatus;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_member", columnList = "member_id"),
    @Index(name = "idx_payment_status", columnList = "status")
})
public class Payment implements Serializable {
    
    @Id
    @Column(name = "payment_id")
    private String paymentId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id", nullable = false)
    private Package package_;
    
    @Column(name = "amount", nullable = false)
    private Double amount;
    
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "receipt_url")
    private String receiptUrl;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Column(name = "failure_reason")
    private String failureReason;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
