package com.gym.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.CreateMemberRequest;
import com.gym.model.Member;
import com.gym.model.MemberStatus;
import com.gym.model.UserRole;
import com.gym.repository.AppUserRepository;
import com.gym.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AppUserRepository userRepository;

    public MemberService(MemberRepository memberRepository, AppUserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    public Member create(CreateMemberRequest request) {
        memberRepository.findByEmail(request.email()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        });
        memberRepository.findByPhone(request.phone()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone already exists");
        });

        Member member = Member.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .joinDate(LocalDate.now())
                .status(MemberStatus.ACTIVE)
                .build();
        return memberRepository.save(member);
    }

    public Member createForAuthUser(String name, String email, String phone, Long trainerUserId) {
        memberRepository.findByEmail(email).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        });
        memberRepository.findByPhone(phone).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone already exists");
        });

        if (trainerUserId != null) {
            var trainer = userRepository.findById(trainerUserId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid trainerUserId"));
            if (trainer.getRole() != UserRole.TRAINER) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "trainerUserId must be a TRAINER user");
            }
        }

        Member member = Member.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .joinDate(LocalDate.now())
                .status(MemberStatus.ACTIVE)
                .trainerUserId(trainerUserId)
                .build();
        return memberRepository.save(member);
    }

    public List<Member> list() {
        return memberRepository.findAll();
    }

    public Member getById(Long id) {
        Objects.requireNonNull(id, "id");
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
    }

    public Member updateStatus(Long id, String status) {
        Member member = getById(id);
        try {
            member.setStatus(MemberStatus.fromValue(status));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid member status");
        }
        return memberRepository.save(member);
    }

    public Member requireAssignedToTrainer(Long memberId, Long trainerUserId) {
        Member member = getById(memberId);
        if (member.getTrainerUserId() == null || !member.getTrainerUserId().equals(trainerUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Member is not assigned to this trainer");
        }
        return member;
    }
}
