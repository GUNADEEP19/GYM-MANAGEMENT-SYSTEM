package com.gym.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.ApiResponse;
import com.gym.dto.PaymentResponse;
import com.gym.dto.ProcessPaymentRequest;
import com.gym.dto.ReceiptResponse;
import com.gym.model.Payment;
import com.gym.model.UserRole;
import com.gym.security.CurrentUser;
import com.gym.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
@Validated
public class PaymentController {

    private final PaymentService paymentService;
    private final CurrentUser currentUser;

    public PaymentController(PaymentService paymentService, CurrentUser currentUser) {
        this.paymentService = paymentService;
        this.currentUser = currentUser;
    }

    @PostMapping("/process")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<PaymentResponse> process(@Valid @RequestBody ProcessPaymentRequest request) {
        Long memberId = currentUser.requireMemberId();
        Payment payment = paymentService.process(memberId, request);
        return ApiResponse.ok(toResponse(payment));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<List<PaymentResponse>> myPayments() {
        Long memberId = currentUser.requireMemberId();
        List<PaymentResponse> resp = paymentService.listByMember(memberId).stream().map(this::toResponse).toList();
        return ApiResponse.ok(resp);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<PaymentResponse>> list(@RequestParam(required = false) Long memberId) {
        List<Payment> list = memberId == null ? paymentService.listAll() : paymentService.listByMember(memberId);
        return ApiResponse.ok(list.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{paymentId}/receipt")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    public ApiResponse<ReceiptResponse> receipt(@PathVariable Long paymentId) {
        var user = currentUser.requireUser();
        boolean isAdmin = user.getRole() == UserRole.ADMIN;
        Long memberId = isAdmin ? null : currentUser.requireMemberId();

        Payment payment = paymentService.getById(paymentId);

        if (!isAdmin && !payment.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot view another member's receipt");
        }

        var pkg = payment.getGymPackage();
        ReceiptResponse resp = new ReceiptResponse(
                paymentService.receiptNumberFor(payment),
                payment.getId(),
                payment.getMember().getId(),
                payment.getMember().getName(),
                pkg != null ? pkg.getId() : null,
                pkg != null ? pkg.getName() : null,
                payment.getBaseAmount(),
                payment.getDiscountAmount(),
                payment.getDiscountCode(),
                payment.getAmount(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                payment.getPaidAt());

        return ApiResponse.ok(resp);
    }

    private PaymentResponse toResponse(Payment p) {
        var pkg = p.getGymPackage();
        return new PaymentResponse(
                p.getId(),
                p.getAmount(),
                p.getPaidAt(),
                p.getMethod().name(),
                p.getStatus().name(),
                paymentService.receiptNumberFor(p),
                pkg != null ? pkg.getId() : null,
                pkg != null ? pkg.getName() : null,
                p.getDiscountCode(),
                p.getBaseAmount(),
                p.getDiscountAmount());
    }
}
