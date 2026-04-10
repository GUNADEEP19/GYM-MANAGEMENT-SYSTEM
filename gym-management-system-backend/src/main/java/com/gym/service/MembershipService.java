package com.gym.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.AssignMembershipRequest;
import com.gym.model.GymPackage;
import com.gym.model.Member;
import com.gym.model.Membership;
import com.gym.model.MembershipStatus;
import com.gym.repository.MembershipRepository;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MemberService memberService;
    private final GymPackageService packageService;

    public MembershipService(MembershipRepository membershipRepository, MemberService memberService,
            GymPackageService packageService) {
        this.membershipRepository = membershipRepository;
        this.memberService = memberService;
        this.packageService = packageService;
    }

    public Membership assign(AssignMembershipRequest request) {
        Member member = memberService.getById(request.memberId());
        GymPackage gymPackage = packageService.getById(request.packageId());

        LocalDate startDate = request.startDate() != null ? request.startDate() : LocalDate.now();

        Membership membership = new Membership();
        membership.setMember(member);
        membership.setGymPackage(gymPackage);
        membership.setStartDate(startDate);
        membership.setEndDate(startDate.plusMonths(gymPackage.getDurationMonths()));
        membership.setStatus(MembershipStatus.ACTIVE);

        return membershipRepository.save(membership);
    }

    public Membership latestForMember(Long memberId) {
        return membershipRepository.findTopByMemberIdOrderByEndDateDesc(memberId).orElse(null);
    }

    public boolean isActiveForMemberOn(Long memberId, LocalDate date) {
        return membershipRepository
                .findTopByMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByEndDateDesc(
                        memberId, MembershipStatus.ACTIVE, date, date)
                .isPresent();
    }

    public Membership requireActiveForMemberOn(Long memberId, LocalDate date) {
        return membershipRepository
                .findTopByMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByEndDateDesc(
                        memberId, MembershipStatus.ACTIVE, date, date)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Active membership required"));
    }

    public List<Membership> listAll() {
        return membershipRepository.findAll();
    }

    public List<Membership> listByMember(Long memberId) {
        return membershipRepository.findByMemberId(memberId);
    }
}
