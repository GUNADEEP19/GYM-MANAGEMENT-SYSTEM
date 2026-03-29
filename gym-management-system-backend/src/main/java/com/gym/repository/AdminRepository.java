package com.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
