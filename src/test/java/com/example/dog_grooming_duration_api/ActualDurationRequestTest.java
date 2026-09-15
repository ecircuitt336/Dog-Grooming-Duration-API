package com.example.dog_grooming_duration_api;

import com.example.dog_grooming_duration_api.dtos.ActualDurationRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActualDurationRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void validDurationIsAccepted() {

        ActualDurationRequest request = new ActualDurationRequest();
        request.setActualDurationMinutes(103);

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void minimumDurationIsAccepted() {

        ActualDurationRequest request = new ActualDurationRequest();
        request.setActualDurationMinutes(1);

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void zeroDurationIsRejected() {

        ActualDurationRequest request = new ActualDurationRequest();
        request.setActualDurationMinutes(0);

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void nullDurationIsRejected() {

        ActualDurationRequest request = new ActualDurationRequest();
        request.setActualDurationMinutes(null);

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}