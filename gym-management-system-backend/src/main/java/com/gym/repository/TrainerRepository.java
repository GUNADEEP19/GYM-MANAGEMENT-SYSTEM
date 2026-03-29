package com.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.model.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, String> {
}
