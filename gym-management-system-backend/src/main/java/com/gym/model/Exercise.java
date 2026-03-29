package com.gym.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @Column(name = "exercise_id", nullable = false, updatable = false)
    private String exerciseId;

    @Column(nullable = false)
    private String exerciseName;

    @Column(nullable = false)
    private String bodyPart;

    @Column(nullable = false)
    private String equipmentRequired;

    @Column(nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(columnDefinition = "TEXT")
    private String videoUrl;

    @Column(nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @PrePersist
    public void prePersist() {
        if (this.exerciseId == null || this.exerciseId.isBlank()) {
            this.exerciseId = UUID.randomUUID().toString();
        }
    }
}
