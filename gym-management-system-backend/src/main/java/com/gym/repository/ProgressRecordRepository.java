package com.gym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.model.ProgressRecord;

public interface ProgressRecordRepository extends JpaRepository<ProgressRecord, Long> {
    List<ProgressRecord> findByMemberIdOrderByCreatedAtDesc(Long memberId);
    Optional<ProgressRecord> findTopByMemberIdOrderByCreatedAtDesc(Long memberId);
}
