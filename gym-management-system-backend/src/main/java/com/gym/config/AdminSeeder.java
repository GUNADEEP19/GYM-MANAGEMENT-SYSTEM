package com.gym.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gym.model.AppUser;
import com.gym.model.UserRole;
import com.gym.repository.AppUserRepository;

@Component
public class AdminSeeder implements ApplicationRunner {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String name;
    private final String email;
    private final String phone;
    private final String password;

    public AdminSeeder(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.name}") String name,
            @Value("${app.admin.email}") String email,
            @Value("${app.admin.phone}") String phone,
            @Value("${app.admin.password}") String password) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmail(email)) {
            return;
        }

        AppUser admin = AppUser.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .role(UserRole.ADMIN)
                .passwordHash(passwordEncoder.encode(password))
                .build();
        userRepository.save(admin);
    }
}
