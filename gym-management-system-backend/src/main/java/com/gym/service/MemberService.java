package com.gym.service;

import java.util.List;

import com.gym.dto.CreateMemberRequest;
import com.gym.model.Member;

/**
 * Component interface for the Decorator pattern (GoF – Structural).
 *
 * <p>Defines the contract for member management operations. Concrete
 * implementations include:
 * <ul>
 *   <li>{@link DefaultMemberService}  – the real business-logic implementation.</li>
 *   <li>{@link AuditingMemberServiceDecorator} – structural decorator that wraps
 *       DefaultMemberService and adds cross-cutting audit logging without
 *       modifying the core logic.</li>
 * </ul>
 */
public interface MemberService {

    Member create(CreateMemberRequest request);

    Member createForAuthUser(String name, String email, String phone, Long trainerUserId);

    List<Member> list();

    Member getById(Long id);

    Member updateStatus(Long id, String status);

    Member requireAssignedToTrainer(Long memberId, Long trainerUserId);
}
