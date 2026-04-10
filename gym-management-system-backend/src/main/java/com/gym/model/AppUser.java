package com.gym.model;

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
@Table(name = "users")
public class AppUser {

    // -----------------------------------------------------------------------
    // Builder Pattern (GoF – Creational)
    // AppUser bundles authentication credentials (passwordHash) with profile
    // data and a role. The builder makes it impossible to forget passwordHash
    // or role, both of which have no sensible default.
    // -----------------------------------------------------------------------
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String email;
        private String phone;
        private String passwordHash;
        private UserRole role;
        private Long memberId;   // optional – set only for MEMBER-role users

        private Builder() {}

        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        public Builder role(UserRole role) { this.role = role; return this; }
        public Builder memberId(Long memberId) { this.memberId = memberId; return this; }

        @NonNull
        public AppUser build() {
            java.util.Objects.requireNonNull(name, "AppUser.name is required");
            java.util.Objects.requireNonNull(email, "AppUser.email is required");
            java.util.Objects.requireNonNull(phone, "AppUser.phone is required");
            java.util.Objects.requireNonNull(passwordHash, "AppUser.passwordHash is required");
            java.util.Objects.requireNonNull(role, "AppUser.role is required");
            AppUser u = new AppUser();
            u.name = this.name;
            u.email = this.email;
            u.phone = this.phone;
            u.passwordHash = this.passwordHash;
            u.role = this.role;
            u.memberId = this.memberId;
            return u;
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
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // For MEMBER users we create a Member profile row and store its id here.
    @Column(name = "member_id")
    private Long memberId;

    /** Protected: JPA requires a no-arg constructor; application code must use AppUser.builder(). */
    protected AppUser() {}

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
