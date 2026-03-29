package com.gym.model;

import java.time.LocalDateTime;
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
@Table(name = "progress")
public class Progress {

    @Id
    @Column(name = "progress_id", nullable = false, updatable = false)
    private String progressId;

    @Column(nullable = false)
    private Integer weekNumber;

    @Column(nullable = false)
    private Integer exercisesDone;

    @Column(columnDefinition = "TEXT")
    private String progressNotes;

    @Column(nullable = false)
    private LocalDateTime recordedDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @PrePersist
    public void prePersist() {
        if (this.progressId == null || this.progressId.isBlank()) {
            this.progressId = UUID.randomUUID().toString();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }
}
