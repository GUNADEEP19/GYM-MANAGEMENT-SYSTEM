package com.gym.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gym.model.Admin;
import com.gym.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String adminName;
    private final String adminEmail;
    private final String adminPhone;
    private final String adminPassword;

    public AdminSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.name:Super Admin}") String adminName,
            @Value("${app.admin.email:admin@gym.com}") String adminEmail,
            @Value("${app.admin.phone:9999999999}") String adminPhone,
            @Value("${app.admin.password:admin123}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPhone = adminPhone;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        boolean exists = userRepository.findByEmail(adminEmail)
                .map(u -> u instanceof Admin)
                .orElse(false);
        if (exists) {
            return;
        }

        // If email exists but isn't admin, don't overwrite it.
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            log.warn("Admin seeding skipped: email already exists but is not ADMIN: {}", adminEmail);
            return;
        }

        Admin admin = new Admin();
        admin.setName(adminName);
        admin.setEmail(adminEmail);
        admin.setPhone(adminPhone);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));

        userRepository.save(admin);
        log.info("Seeded predefined ADMIN user: {}", adminEmail);
    }
}

