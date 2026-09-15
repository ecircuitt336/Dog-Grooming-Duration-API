package com.example.dog_grooming_duration_api.entities;

import com.example.dog_grooming_duration_api.enums.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @Generated
    private UUID id;

    @Generated
    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "breed_id")
    private Breed breed;

    @Enumerated(EnumType.STRING)
    @Column(name = "coat_length")
    private CoatLength coatLength;

    @Enumerated(EnumType.STRING)
    @Column(name = "coat_texture")
    private CoatTexture coatTexture;

    @Enumerated(EnumType.STRING)
    @Column(name = "coat_structure")
    private CoatStructure coatStructure;

    @Enumerated(EnumType.STRING)
    @Column(name = "matting_severity")
    private MattingSeverity mattingSeverity;

    @Enumerated(EnumType.STRING)
    @Column(name = "behaviour")
    private Behaviour behaviour;

    @Enumerated(EnumType.STRING)
    @Column(name = "service")
    private Service service;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    @Column(name = "groomer_experience_years")
    private BigDecimal groomerExperienceYears;

    @Column(name = "estimator_version")
    private String estimatorVersion;

    @Column(name = "estimated_minutes")
    private Integer estimatedMinutes;

    @Column(name = "lower_bound_minutes")
    private Integer lowerBoundMinutes;

    @Column(name = "upper_bound_minutes")
    private Integer upperBoundMinutes;

    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

    @Column(name = "completed_at")
    private Instant completedAt;

    // Required by JPA/Hibernate when creating entity instances.
    protected Appointment() {
    }

    public Appointment(
            Breed breed,
            BigDecimal weightKg,
            CoatLength coatLength,
            CoatTexture coatTexture,
            CoatStructure coatStructure,
            MattingSeverity mattingSeverity,
            Behaviour behaviour,
            Service service,
            BigDecimal groomerExperienceYears,
            String estimatorVersion,
            Integer estimatedMinutes,
            Integer lowerBoundMinutes,
            Integer upperBoundMinutes
            ) {
        this.breed = breed;
        this.weightKg = weightKg;
        this.coatLength = coatLength;
        this.coatTexture = coatTexture;
        this.coatStructure = coatStructure;
        this.mattingSeverity = mattingSeverity;
        this.behaviour = behaviour;
        this.service = service;
        this.groomerExperienceYears = groomerExperienceYears;
        this.estimatorVersion = estimatorVersion;
        this.estimatedMinutes = estimatedMinutes;
        this.lowerBoundMinutes = lowerBoundMinutes;
        this.upperBoundMinutes = upperBoundMinutes;
    }

    // Methods
    public void complete(int actualDurationMinutes) {
        if (actualDurationMinutes <= 0) {
            throw new IllegalArgumentException("Actual duration must be greater than 0 minutes.");
        }

        if (this.actualDurationMinutes != null) {
            throw new IllegalStateException("Appointment has already been completed.");
        }

        this.actualDurationMinutes = actualDurationMinutes;
        this.completedAt = Instant.now();
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Breed getBreed() {
        return breed;
    }

    public CoatLength getCoatLength() {
        return coatLength;
    }

    public CoatTexture getCoatTexture() {
        return coatTexture;
    }

    public CoatStructure getCoatStructure() {
        return coatStructure;
    }

    public MattingSeverity getMattingSeverity() {
        return mattingSeverity;
    }

    public Behaviour getBehaviour() {
        return behaviour;
    }

    public Service getService() {
        return service;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public BigDecimal getGroomerExperienceYears() {
        return groomerExperienceYears;
    }

    public String getEstimatorVersion() {
        return estimatorVersion;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public Integer getLowerBoundMinutes() {
        return lowerBoundMinutes;
    }

    public Integer getUpperBoundMinutes() {
        return upperBoundMinutes;
    }

    public Integer getActualDurationMinutes() {
        return actualDurationMinutes;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
