package com.gym.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gym")
public class GymManagementSystemBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymManagementSystemBackendApplication.class, args);
	}

}
