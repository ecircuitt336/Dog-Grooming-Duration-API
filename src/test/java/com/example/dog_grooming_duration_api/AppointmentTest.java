package com.example.dog_grooming_duration_api;

import com.example.dog_grooming_duration_api.entities.Appointment;
import com.example.dog_grooming_duration_api.enums.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppointmentTest {

    @Test
    void shouldCompleteAppointment() {
        Appointment appointment = new Appointment(
                null,
                new BigDecimal("12.5"),
                CoatLength.LONG,
                CoatTexture.WAVY,
                CoatStructure.SINGLE,
                MattingSeverity.MODERATE,
                Behaviour.GOOD,
                Service.FULL_GROOM_AND_CLIP,
                new BigDecimal("2.0"),
                "RULES_V1",
                95,
                80,
                110
        );

        appointment.complete(100);

        assertEquals(100, appointment.getActualDurationMinutes());
        assertNotNull(appointment.getCompletedAt());

        assertEquals(95, appointment.getEstimatedMinutes());
        assertEquals(80, appointment.getLowerBoundMinutes());
        assertEquals(110, appointment.getUpperBoundMinutes());
    }

    @Test
    void shouldRejectInvalidActualDuration() {
        Appointment appointment = new Appointment(
                null,
                new BigDecimal("12.5"),
                CoatLength.LONG,
                CoatTexture.WAVY,
                CoatStructure.SINGLE,
                MattingSeverity.MODERATE,
                Behaviour.GOOD,
                Service.FULL_GROOM_AND_CLIP,
                new BigDecimal("2.0"),
                "RULES_V1",
                95,
                80,
                110
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> appointment.complete(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> appointment.complete(-10)
        );
    }

    @Test
    void shouldRejectCompletingAppointmentTwice() {
        Appointment appointment = new Appointment(
                null,
                new BigDecimal("12.5"),
                CoatLength.LONG,
                CoatTexture.WAVY,
                CoatStructure.SINGLE,
                MattingSeverity.MODERATE,
                Behaviour.GOOD,
                Service.FULL_GROOM_AND_CLIP,
                new BigDecimal("2.0"),
                "RULES_V1",
                95,
                80,
                110
        );

        appointment.complete(100);

        assertThrows(
                IllegalStateException.class,
                () -> appointment.complete(120)
        );

        assertEquals(100, appointment.getActualDurationMinutes());
    }
}
