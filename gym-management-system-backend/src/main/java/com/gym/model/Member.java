package com.gym.model;

import java.time.LocalDate;

import org.springframework.lang.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class Member {

    // -----------------------------------------------------------------------
    // Builder Pattern (GoF – Creational)
    // Enforces that Member objects are always fully constructed via the builder,
    // preventing partially-initialised instances that raw setters allow.
    // -----------------------------------------------------------------------
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String email;
        private String phone;
        private java.time.LocalDate joinDate;
        private MemberStatus status;
        private Long trainerUserId;

        private Builder() {}

        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder joinDate(java.time.LocalDate joinDate) { this.joinDate = joinDate; return this; }
        public Builder status(MemberStatus status) { this.status = status; return this; }
        public Builder trainerUserId(Long trainerUserId) { this.trainerUserId = trainerUserId; return this; }

        @NonNull
        public Member build() {
            java.util.Objects.requireNonNull(name, "Member.name is required");
            java.util.Objects.requireNonNull(email, "Member.email is required");
            java.util.Objects.requireNonNull(phone, "Member.phone is required");
            java.util.Objects.requireNonNull(joinDate, "Member.joinDate is required");
            java.util.Objects.requireNonNull(status, "Member.status is required");
            Member m = new Member();
            m.name = this.name;
            m.email = this.email;
            m.phone = this.phone;
            m.joinDate = this.joinDate;
            m.status = this.status;
            m.trainerUserId = this.trainerUserId;
            return m;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    // If assigned, this links a Member to a TRAINER user's id.
    @Column(name = "trainer_user_id")
    private Long trainerUserId;

    /** Protected: JPA requires a no-arg constructor; application code must use Member.builder(). */
    protected Member() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public Long getTrainerUserId() {
        return trainerUserId;
    }

    public void setTrainerUserId(Long trainerUserId) {
        this.trainerUserId = trainerUserId;
    }
}
