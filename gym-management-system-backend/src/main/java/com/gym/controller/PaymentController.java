package com.gym.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.PaymentRequest;
import com.gym.dto.PaymentResponse;
import com.gym.model.Member;
import com.gym.model.Package;
import com.gym.model.Payment;
import com.gym.repository.MemberRepository;
import com.gym.repository.PackageRepository;
import com.gym.repository.PaymentRepository;
import com.gym.service.payment.PaymentManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    
    private final PaymentManager paymentManager;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final PackageRepository packageRepository;
    
    @Autowired
    public PaymentController(PaymentManager paymentManager,
                           PaymentRepository paymentRepository,
                           MemberRepository memberRepository,
                           PackageRepository packageRepository) {
        this.paymentManager = paymentManager;
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
        this.packageRepository = packageRepository;
    }
    
    /**
     * Process payment
     * POST /api/payment/pay
     * @param paymentRequest Payment details
     * @return PaymentResponse with transaction status
     */
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        
        // Validate request
        if (paymentRequest == null || paymentRequest.getMemberId() == null || 
            paymentRequest.getPackageId() == null || paymentRequest.getPaymentMethod() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required payment fields");
        }

        String memberId = Objects.requireNonNull(paymentRequest.getMemberId());
        String packageId = Objects.requireNonNull(paymentRequest.getPackageId());
        
        // Fetch member
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        
        // Fetch package
        Package package_ = packageRepository.findById(packageId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));
        
        // Create payment object
        Payment payment = new Payment();
        payment.setMember(member);
        payment.setPackage_(package_);
        payment.setAmount(paymentRequest.getAmount() != null ? paymentRequest.getAmount() : package_.getPrice());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        
        // Process payment through manager
        Payment processedPayment = paymentManager.processPayment(payment);
        
        // Return response
        return ResponseEntity.ok(mapToResponse(processedPayment));
    }
    
    /**
     * Get payment status
     * GET /api/payment/status/{id}
     * @param paymentId Payment ID
     * @return PaymentResponse with current status
     */
    @GetMapping("/status/{id}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable("id") String paymentId) {
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        
        return ResponseEntity.ok(mapToResponse(payment));
    }
    
    /**
     * Map Payment entity to PaymentResponse DTO
     * @param payment Payment entity
     * @return PaymentResponse
     */
    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
            .paymentId(payment.getPaymentId())
            .memberId(payment.getMember().getUserId())
            .packageId(payment.getPackage_().getPackageId())
            .amount(payment.getAmount())
            .paymentMethod(payment.getPaymentMethod())
            .status(payment.getStatus())
            .transactionId(payment.getTransactionId())
            .receiptUrl(payment.getReceiptUrl())
            .paymentDate(payment.getPaymentDate())
            .failureReason(payment.getFailureReason())
            .createdAt(payment.getCreatedAt())
            .updatedAt(payment.getUpdatedAt())
            .build();
    }
}
