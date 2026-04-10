package com.gym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gym.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByMemberId(Long memberId);

    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.status = com.gym.model.PaymentStatus.SUCCESS")
    double sumSuccessfulRevenue();
}
