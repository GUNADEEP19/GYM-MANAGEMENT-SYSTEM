package com.gym.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workout_plans")
public class WorkoutPlan {

    @Id
    @Column(name = "plan_id", nullable = false, updatable = false)
    private String planId;

    @Column(nullable = false)
    private String planName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String fitnessGoal;

    @Column(nullable = false)
    private String difficultyLevel;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Integer durationWeeks;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Exercise> exercises = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.planId == null || this.planId.isBlank()) {
            this.planId = UUID.randomUUID().toString();
        }
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }
}
