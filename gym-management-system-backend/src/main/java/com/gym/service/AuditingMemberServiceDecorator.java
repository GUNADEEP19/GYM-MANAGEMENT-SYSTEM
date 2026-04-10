package com.gym.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.gym.dto.CreateMemberRequest;
import com.gym.model.Member;

/**
 * Structural Decorator (GoF – Structural) for {@link MemberService}.
 *
 * <p><b>Pattern Role:</b> Concrete Decorator<br>
 * <b>Component Interface:</b> {@link MemberService}<br>
 * <b>Concrete Component:</b> {@link DefaultMemberService}
 *
 * <p>This decorator transparently wraps every {@link MemberService} operation
 * and adds cross-cutting audit logging without touching the business logic in
 * {@link DefaultMemberService}. Spring injects this bean wherever
 * {@link MemberService} is declared, because it is marked {@code @Primary}.
 *
 * <p><b>Open/Closed Principle in action:</b> New cross-cutting concerns
 * (e.g., metrics, rate-limiting, caching) can be added by stacking another
 * decorator — neither this class nor {@link DefaultMemberService} needs to
 * change.
 */
@Primary
@Service
public class AuditingMemberServiceDecorator implements MemberService {

    private static final Logger log = LoggerFactory.getLogger(AuditingMemberServiceDecorator.class);

    /** The wrapped Concrete Component — injected specifically by qualifier. */
    private final MemberService delegate;

    public AuditingMemberServiceDecorator(
            @Qualifier("defaultMemberService") MemberService delegate) {
        this.delegate = delegate;
    }

    // ------------------------------------------------------------------
    // Write operations – log before + after so mutations are fully traced
    // ------------------------------------------------------------------

    @Override
    public Member create(CreateMemberRequest request) {
        log.info("[AUDIT] Creating member | email={} phone={}", request.email(), request.phone());
        Member result = delegate.create(request);
        log.info("[AUDIT] Member created   | id={} name=\"{}\"", result.getId(), result.getName());
        return result;
    }

    @Override
    public Member createForAuthUser(String name, String email, String phone, Long trainerUserId) {
        log.info("[AUDIT] Creating member for auth user | email={} trainerUserId={}", email, trainerUserId);
        Member result = delegate.createForAuthUser(name, email, phone, trainerUserId);
        log.info("[AUDIT] Auth-user member created | id={} name=\"{}\"", result.getId(), result.getName());
        return result;
    }

    @Override
    public Member updateStatus(Long id, String status) {
        log.info("[AUDIT] Member status change requested | memberId={} newStatus={}", id, status);
        Member result = delegate.updateStatus(id, status);
        log.info("[AUDIT] Member status updated | memberId={} status={}", result.getId(), result.getStatus());
        return result;
    }

    // ------------------------------------------------------------------
    // Read operations – log at DEBUG so they don't flood production logs
    // ------------------------------------------------------------------

    @Override
    public List<Member> list() {
        log.debug("[AUDIT] Listing all members");
        List<Member> result = delegate.list();
        log.debug("[AUDIT] Listed {} member(s)", result.size());
        return result;
    }

    @Override
    public Member getById(Long id) {
        log.debug("[AUDIT] Fetching member | memberId={}", id);
        return delegate.getById(id);
    }

    @Override
    public Member requireAssignedToTrainer(Long memberId, Long trainerUserId) {
        log.debug("[AUDIT] Verifying trainer assignment | memberId={} trainerUserId={}", memberId, trainerUserId);
        return delegate.requireAssignedToTrainer(memberId, trainerUserId);
    }
}
