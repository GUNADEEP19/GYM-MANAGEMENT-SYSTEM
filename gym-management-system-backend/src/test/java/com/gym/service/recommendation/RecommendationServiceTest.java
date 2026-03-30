package com.gym.service.recommendation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gym.dto.RecommendationResponse;
import com.gym.model.Member;
import com.gym.model.Progress;
import com.gym.repository.MemberRepository;
import com.gym.repository.ProgressRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Recommendation Service Strategy Tests")
class RecommendationServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProgressRepository progressRepository;

    private RecommendationService recommendationService;

    private Member testMember;
    private Progress testProgress;

    @BeforeEach
    void setUp() {
        // Setup strategies
        List<WorkoutRecommendationStrategy> strategies = Arrays.asList(
                new WeightLossStrategy(),
                new MuscleGainStrategy(),
                new GeneralFitnessStrategy());

        recommendationService = new RecommendationService(strategies, memberRepository, progressRepository);

        testMember = new Member();
        testMember.setUserId("member-123");
        testMember.setName("Test Member");

        testProgress = new Progress();
        testProgress.setMember(testMember);
    }

    @Test
    @DisplayName("Should use WeightLossStrategy for BMI >= 25")
    void testWeightLossStrategy() {
        testProgress.setBmi(26.5);

        when(memberRepository.findById("member-123")).thenReturn(Optional.of(testMember));
        when(progressRepository.findProgressByMemberOrderByDateDesc("member-123"))
                .thenReturn(Arrays.asList(testProgress));

        RecommendationResponse response = recommendationService.generateRecommendation("member-123");

        assertEquals("WeightLossStrategy", response.getSelectedStrategy());
        assertEquals(26.5, response.getCurrentBmi());
    }

    @Test
    @DisplayName("Should use MuscleGainStrategy for BMI < 18.5")
    void testMuscleGainStrategy() {
        testProgress.setBmi(17.0);

        when(memberRepository.findById("member-123")).thenReturn(Optional.of(testMember));
        when(progressRepository.findProgressByMemberOrderByDateDesc("member-123"))
                .thenReturn(Arrays.asList(testProgress));

        RecommendationResponse response = recommendationService.generateRecommendation("member-123");

        assertEquals("MuscleGainStrategy", response.getSelectedStrategy());
        assertEquals(17.0, response.getCurrentBmi());
    }

    @Test
    @DisplayName("Should use GeneralFitnessStrategy for normal BMI")
    void testGeneralFitnessStrategy() {
        testProgress.setBmi(22.0);

        when(memberRepository.findById("member-123")).thenReturn(Optional.of(testMember));
        when(progressRepository.findProgressByMemberOrderByDateDesc("member-123"))
                .thenReturn(Arrays.asList(testProgress));

        RecommendationResponse response = recommendationService.generateRecommendation("member-123");

        assertEquals("GeneralFitnessStrategy", response.getSelectedStrategy());
        assertEquals(22.0, response.getCurrentBmi());
    }

    @Test
    @DisplayName("Should return generic 'NoStrategy' response if no progress exists")
    void testNoProgressData() {
        when(memberRepository.findById("member-123")).thenReturn(Optional.of(testMember));
        when(progressRepository.findProgressByMemberOrderByDateDesc("member-123"))
                .thenReturn(new ArrayList<>());

        RecommendationResponse response = recommendationService.generateRecommendation("member-123");

        assertEquals("NoStrategy", response.getSelectedStrategy());
        assertEquals(0.0, response.getCurrentBmi());
    }
}
