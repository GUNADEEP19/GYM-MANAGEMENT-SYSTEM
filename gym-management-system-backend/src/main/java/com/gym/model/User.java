package com.gym.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_email", columnList = "email")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @PrePersist
    public void prePersist() {
        if (this.userId == null || this.userId.isBlank()) {
            this.userId = UUID.randomUUID().toString();
        }
    }
}
