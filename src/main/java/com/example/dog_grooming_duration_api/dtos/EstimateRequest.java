package com.example.dog_grooming_duration_api.dtos;

import com.example.dog_grooming_duration_api.enums.*;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * This class defines the data a client must provide when asking the API for a grooming-duration estimate.
 */

public class EstimateRequest {

    // Definitions of data that must be provided
    @NotBlank
    private String breed;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax("200.0")
    private BigDecimal weightKg;

    @NotNull
    private CoatLength coatLength;
    @NotNull
    private CoatTexture coatTexture;
    @NotNull
    private CoatStructure coatStructure;
    @NotNull
    private MattingSeverity mattingSeverity;
    @NotNull
    private Behaviour behaviour;
    @NotNull
    private Service service;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal groomerExperienceYears;

    public EstimateRequest() {
    }

    // Getters & Setters

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public CoatLength getCoatLength() {
        return coatLength;
    }

    public void setCoatLength(CoatLength coatLength) {
        this.coatLength = coatLength;
    }

    public CoatTexture getCoatTexture() {
        return coatTexture;
    }

    public void setCoatTexture(CoatTexture coatTexture) {
        this.coatTexture = coatTexture;
    }

    public CoatStructure getCoatStructure() {
        return coatStructure;
    }

    public void setCoatStructure(CoatStructure coatStructure) {
        this.coatStructure = coatStructure;
    }

    public MattingSeverity getMattingSeverity() {
        return mattingSeverity;
    }

    public void setMattingSeverity(MattingSeverity mattingSeverity) {
        this.mattingSeverity = mattingSeverity;
    }

    public Behaviour getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(Behaviour behaviour) {
        this.behaviour = behaviour;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public BigDecimal getGroomerExperienceYears() {
        return groomerExperienceYears;
    }

    public void setGroomerExperienceYears(BigDecimal groomerExperienceYears) {
        this.groomerExperienceYears = groomerExperienceYears;
    }
}
