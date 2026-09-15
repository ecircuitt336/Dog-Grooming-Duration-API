package com.example.dog_grooming_duration_api.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "breeds")
public class Breed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "active")
    private boolean active;

    // Required by JPA/Hibernate when creating entity instances.
    protected Breed() {
    }

    // Getters

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public boolean isActive() {
        return active;
    }
}
