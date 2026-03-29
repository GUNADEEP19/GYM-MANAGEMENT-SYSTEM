package com.gym.repository;

import com.gym.model.Payment;
import com.gym.dto.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    
    /**
     * Find payments by member ID
     * @param memberId Member ID
     * @return List of payments
     */
    @Query("SELECT p FROM Payment p WHERE p.member.userId = :memberId")
    List<Payment> findByMemberId(@Param("memberId") String memberId);
    
    /**
     * Find payments by member and status
     * @param memberId Member ID
     * @param status Payment status
     * @return List of payments with specified status
     */
    @Query("SELECT p FROM Payment p WHERE p.member.userId = :memberId AND p.status = :status")
    List<Payment> findByMemberIdAndStatus(@Param("memberId") String memberId, @Param("status") PaymentStatus status);
    
    /**
     * Find payments by status
     * @param status Payment status
     * @return List of payments with specified status
     */
    List<Payment> findByStatus(PaymentStatus status);
    
    /**
     * Find payments by payment method
     * @param paymentMethod Payment method
     * @return List of payments with specified method
     */
    List<Payment> findByPaymentMethod(String paymentMethod);
    
    /**
     * Find payments in date range
     * @param memberId Member ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of payments within date range
     */
    @Query("SELECT p FROM Payment p WHERE p.member.userId = :memberId AND p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsInDateRange(
        @Param("memberId") String memberId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    /**
     * Find successful payments
     * @return List of successful payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'SUCCESS'")
    List<Payment> findSuccessfulPayments();
    
    /**
     * Count payments by status
     * @param status Payment status
     * @return Count of payments
     */
    Long countByStatus(PaymentStatus status);
}
