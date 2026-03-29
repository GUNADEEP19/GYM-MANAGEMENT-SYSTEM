package com.gym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "trainers")
public class Trainer extends User {
}
