package com.example.dog_grooming_duration_api.dtos;

import com.example.dog_grooming_duration_api.enums.*;
import com.example.dog_grooming_duration_api.services.EstimateService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EstimateRequestTest {

    private final Validator validator;

    EstimateRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldAcceptValidRequest() {
        EstimateRequest request = new EstimateRequest();

        request.setBreed("COCKER_SPANIEL");
        request.setWeightKg(new BigDecimal("12.5"));
        request.setCoatLength(CoatLength.LONG);
        request.setCoatTexture(CoatTexture.WAVY);
        request.setCoatStructure(CoatStructure.SINGLE);
        request.setMattingSeverity(MattingSeverity.MODERATE);
        request.setBehaviour(Behaviour.GOOD);
        request.setService(Service.FULL_GROOM_AND_CLIP);
        request.setGroomerExperienceYears(new BigDecimal("2.0"));

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectZeroWeight() {
        EstimateRequest request = new EstimateRequest();

        request.setBreed("COCKER_SPANIEL");
        request.setWeightKg(BigDecimal.ZERO);
        request.setCoatLength(CoatLength.LONG);
        request.setCoatTexture(CoatTexture.WAVY);
        request.setCoatStructure(CoatStructure.SINGLE);
        request.setMattingSeverity(MattingSeverity.MODERATE);
        request.setBehaviour(Behaviour.GOOD);
        request.setService(Service.FULL_GROOM_AND_CLIP);
        request.setGroomerExperienceYears(new BigDecimal("2.0"));

        var violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("weightKg")
                        )
        );
    }

    @Test
    void shouldAcceptMaximumWeight() {
        EstimateRequest request = new EstimateRequest();

        request.setBreed("COCKER_SPANIEL");
        request.setWeightKg(new BigDecimal("200.0"));
        request.setCoatLength(CoatLength.LONG);
        request.setCoatTexture(CoatTexture.WAVY);
        request.setCoatStructure(CoatStructure.SINGLE);
        request.setMattingSeverity(MattingSeverity.MODERATE);
        request.setBehaviour(Behaviour.GOOD);
        request.setService(Service.FULL_GROOM_AND_CLIP);
        request.setGroomerExperienceYears(new BigDecimal("2.0"));

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectWeightAboveMaximum() {
        EstimateRequest request = new EstimateRequest();

        request.setBreed("COCKER_SPANIEL");
        request.setWeightKg(new BigDecimal("200.01"));
        request.setCoatLength(CoatLength.LONG);
        request.setCoatTexture(CoatTexture.WAVY);
        request.setCoatStructure(CoatStructure.SINGLE);
        request.setMattingSeverity(MattingSeverity.MODERATE);
        request.setBehaviour(Behaviour.GOOD);
        request.setService(Service.FULL_GROOM_AND_CLIP);
        request.setGroomerExperienceYears(new BigDecimal("2.0"));

        var violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("weightKg")
                        )
        );
    }

    @Test
    void shouldRejectMissingRequiredFields() {
        EstimateRequest request = new EstimateRequest();

        var violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("breed")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("weightKg")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("coatLength")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("coatTexture")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("coatStructure")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("mattingSeverity")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("behaviour")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("service")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("groomerExperienceYears")
                        )
        );
    }

    @Test
    void shouldRejectBlankBreed() {
        EstimateRequest request = new EstimateRequest();

        request.setBreed("   ");
        request.setWeightKg(new BigDecimal("12.5"));
        request.setCoatLength(CoatLength.LONG);
        request.setCoatTexture(CoatTexture.WAVY);
        request.setCoatStructure(CoatStructure.SINGLE);
        request.setMattingSeverity(MattingSeverity.MODERATE);
        request.setBehaviour(Behaviour.GOOD);
        request.setService(Service.FULL_GROOM_AND_CLIP);
        request.setGroomerExperienceYears(new BigDecimal("2.0"));

        var violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("breed")
                        )
        );
    }

    @Test
    void shouldAcceptZeroGroomerExperience() {
        EstimateRequest request = new EstimateRequest();

        request.setBreed("COCKER_SPANIEL");
        request.setWeightKg(new BigDecimal("12.5"));
        request.setCoatLength(CoatLength.LONG);
        request.setCoatTexture(CoatTexture.WAVY);
        request.setCoatStructure(CoatStructure.SINGLE);
        request.setMattingSeverity(MattingSeverity.MODERATE);
        request.setBehaviour(Behaviour.GOOD);
        request.setService(Service.FULL_GROOM_AND_CLIP);
        request.setGroomerExperienceYears(BigDecimal.ZERO);

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectNegativeGroomerExperience() {
        EstimateRequest request = new EstimateRequest();

        request.setBreed("COCKER_SPANIEL");
        request.setWeightKg(new BigDecimal("12.5"));
        request.setCoatLength(CoatLength.LONG);
        request.setCoatTexture(CoatTexture.WAVY);
        request.setCoatStructure(CoatStructure.SINGLE);
        request.setMattingSeverity(MattingSeverity.MODERATE);
        request.setBehaviour(Behaviour.GOOD);
        request.setService(Service.FULL_GROOM_AND_CLIP);
        request.setGroomerExperienceYears(new BigDecimal("-0.1"));

        var violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString()
                                        .equals("groomerExperienceYears")
                        )
        );
    }

    @Test
    void shouldRejectNullEnumFields() {
        EstimateRequest request = new EstimateRequest();

        request.setBreed("COCKER_SPANIEL");
        request.setWeightKg(new BigDecimal("12.5"));
        request.setGroomerExperienceYears(new BigDecimal("2.0"));

        var violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("coatLength")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("coatTexture")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("coatStructure")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("mattingSeverity")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("behaviour")
                        )
        );

        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath().toString().equals("service")
                        )
        );
    }
}