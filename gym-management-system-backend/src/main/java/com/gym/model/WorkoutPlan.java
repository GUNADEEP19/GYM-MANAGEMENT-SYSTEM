package com.gym.model;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "workout_plans")
public class WorkoutPlan {

    // -----------------------------------------------------------------------
    // Builder Pattern (GoF – Creational)
    // WorkoutPlan has 7 fields with one optional (description). The builder
    // makes the mandatory/optional split explicit and prevents the caller
    // from accidentally omitting a required field.
    // -----------------------------------------------------------------------
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long memberId;
        private Long trainerUserId;
        private String planName;
        private String description;          // optional
        private Integer durationWeeks;
        private String difficultyLevel;
        private java.time.LocalDateTime createdAt;

        private Builder() {}

        public Builder memberId(Long memberId) { this.memberId = memberId; return this; }
        public Builder trainerUserId(Long trainerUserId) { this.trainerUserId = trainerUserId; return this; }
        public Builder planName(String planName) { this.planName = planName; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder durationWeeks(Integer durationWeeks) { this.durationWeeks = durationWeeks; return this; }
        public Builder difficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; return this; }
        public Builder createdAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        @NonNull
        public WorkoutPlan build() {
            java.util.Objects.requireNonNull(memberId, "WorkoutPlan.memberId is required");
            java.util.Objects.requireNonNull(trainerUserId, "WorkoutPlan.trainerUserId is required");
            java.util.Objects.requireNonNull(planName, "WorkoutPlan.planName is required");
            java.util.Objects.requireNonNull(durationWeeks, "WorkoutPlan.durationWeeks is required");
            java.util.Objects.requireNonNull(difficultyLevel, "WorkoutPlan.difficultyLevel is required");
            java.util.Objects.requireNonNull(createdAt, "WorkoutPlan.createdAt is required");
            WorkoutPlan p = new WorkoutPlan();
            p.memberId = this.memberId;
            p.trainerUserId = this.trainerUserId;
            p.planName = this.planName;
            p.description = this.description;
            p.durationWeeks = this.durationWeeks;
            p.difficultyLevel = this.difficultyLevel;
            p.createdAt = this.createdAt;
            return p;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long trainerUserId;

    @Column(nullable = false)
    private String planName;

    @Column
    private String description;

    @Column(nullable = false)
    private Integer durationWeeks;

    @Column(nullable = false)
    private String difficultyLevel;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /** Protected: JPA requires a no-arg constructor; application code must use WorkoutPlan.builder(). */
    protected WorkoutPlan() {}

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getTrainerUserId() {
        return trainerUserId;
    }

    public void setTrainerUserId(Long trainerUserId) {
        this.trainerUserId = trainerUserId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(Integer durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
