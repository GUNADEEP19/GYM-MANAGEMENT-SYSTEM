package com.gym.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.gym")
@EntityScan(basePackages = "com.gym.model")
@EnableJpaRepositories(basePackages = "com.gym.repository")
public class GymManagementSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymManagementSystemBackendApplication.class, args);
    }
}
