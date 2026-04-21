package com.gym.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.ProcessPaymentRequest;
import com.gym.model.GymPackage;
import com.gym.model.Member;
import com.gym.model.Membership;
import com.gym.model.MembershipStatus;
import com.gym.model.Payment;
import com.gym.model.PaymentMethod;
import com.gym.model.PaymentStatus;
import com.gym.repository.MembershipRepository;
import com.gym.repository.PaymentRepository;
import com.gym.service.payment.PaymentGateway;
import com.gym.service.payment.PaymentGatewayRequest;
import com.gym.service.payment.PaymentGatewayResult;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberService memberService;
    private final GymPackageService packageService;
    private final MembershipRepository membershipRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentRepository paymentRepository, MemberService memberService,
            GymPackageService packageService,
            MembershipRepository membershipRepository, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.memberService = memberService;
        this.packageService = packageService;
        this.membershipRepository = membershipRepository;
        this.paymentGateway = paymentGateway;
    }

    public Payment process(Long memberId, ProcessPaymentRequest request) {
        Member member = memberService.getById(memberId);
        GymPackage gymPackage = packageService.getById(request.packageId());

        double baseAmount = gymPackage.getPrice();
        double discountAmount = computeDiscountAmount(baseAmount, request.discountCode());
        double requiredAmount = Math.max(0.0, baseAmount - discountAmount);

        Payment payment = new Payment();
        payment.setMember(member);
        payment.setGymPackage(gymPackage);
        payment.setBaseAmount(baseAmount);
        payment.setDiscountAmount(discountAmount);
        payment.setDiscountCode(normalizeDiscountCode(request.discountCode()));
        payment.setAmount(requiredAmount);
        payment.setPaidAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);
        // Loose coupling
        try {
            payment.setMethod(PaymentMethod.fromValue(request.paymentMethod()));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payment method");
        }

        Payment saved = paymentRepository.save(payment);

        PaymentGatewayResult result = paymentGateway.validate(new PaymentGatewayRequest(
                requiredAmount,
                request.amount(),
                request.paymentMethod(),
                request.discountCode()));

        if (!result.approved()) {
            saved.setStatus(PaymentStatus.FAILED);
            return paymentRepository.save(saved);
        }

        saved.setStatus(PaymentStatus.SUCCESS);
        saved = paymentRepository.save(saved);

        // Assign membership based on purchased package (only after SUCCESS).
        Membership membership = new Membership();
        membership.setMember(member);
        membership.setGymPackage(gymPackage);
        var start = java.time.LocalDate.now();
        membership.setStartDate(start);
        membership.setEndDate(start.plusMonths(gymPackage.getDurationMonths()));
        membership.setStatus(MembershipStatus.ACTIVE);
        membershipRepository.save(membership);

        return saved;
    }

    public String receiptNumberFor(Payment payment) {
        // Stable, derivable “receipt id” for the demo use case.
        String date = DateTimeFormatter.BASIC_ISO_DATE.format(payment.getPaidAt().toLocalDate());
        return "RCT-" + payment.getId() + "-" + date;
    }

    private static String normalizeDiscountCode(String code) {
        if (code == null)
            return null;
        String trimmed = code.trim();
        return trimmed.isEmpty() ? null : trimmed.toUpperCase();
    }

    private static double computeDiscountAmount(double baseAmount, String discountCode) {
        String code = normalizeDiscountCode(discountCode);
        if (code == null)
            return 0.0;

        // Demo discounts (keep intentionally simple):
        // - STUDENT10 => 10% off
        // - NEW20 => 20% off
        return switch (code) {
            case "STUDENT10" -> roundMoney(baseAmount * 0.10);
            case "NEW20" -> roundMoney(baseAmount * 0.20);
            default -> 0.0;
        };
    }

    private static double roundMoney(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    public List<Payment> listAll() {
        return paymentRepository.findAll();
    }

    public List<Payment> listByMember(Long memberId) {
        return paymentRepository.findByMemberId(memberId);
    }

    public Payment getById(Long paymentId) {
        Objects.requireNonNull(paymentId, "paymentId");
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }
}
