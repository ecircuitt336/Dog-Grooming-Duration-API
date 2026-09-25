package com.example.dog_grooming_duration_api.entities;

import jakarta.persistence.*;

/**
 * This class represents a breed entity in the breeds table.
 */

// Entity is used to represent an entity
@Entity
// Table is used to represent which table in the database this entity is for
@Table(name = "breeds")
public class Breed {

    // Id means this value is the Primary Key
    @Id
    // GeneratedValue automatically generates a value.
    // IDENTITY means the value is auto-incremented.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Column defines the name of the column this data represents
    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "active")
    private boolean active;

    // Required by JPA when creating entity instances.
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
