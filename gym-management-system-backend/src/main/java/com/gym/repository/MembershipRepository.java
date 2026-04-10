package com.gym.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.model.Membership;
import com.gym.model.MembershipStatus;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByMemberId(Long memberId);

    Optional<Membership> findTopByMemberIdOrderByEndDateDesc(Long memberId);

    Optional<Membership> findTopByMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByEndDateDesc(
            Long memberId,
            MembershipStatus status,
            LocalDate startDate,
            LocalDate endDate);
}
