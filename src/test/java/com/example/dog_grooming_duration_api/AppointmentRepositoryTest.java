package com.example.dog_grooming_duration_api;

import com.example.dog_grooming_duration_api.entities.Appointment;
import com.example.dog_grooming_duration_api.entities.Breed;
import com.example.dog_grooming_duration_api.enums.*;
import com.example.dog_grooming_duration_api.repositories.AppointmentRepository;
import com.example.dog_grooming_duration_api.repositories.BreedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BreedRepository breedRepository;

    @Transactional
    @Test
    void shouldSaveAppointment() {
        var breed = breedRepository.findById(1L);

        assertTrue(breed.isPresent());

        Breed cockerSpaniel = breed.get();

        Appointment appointment = new Appointment(
                cockerSpaniel,
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

        Appointment savedAppointment = appointmentRepository.save(appointment);

        appointment.complete(100);
        appointmentRepository.save(appointment);

        assertNotNull(savedAppointment.getId());
        assertNotNull(savedAppointment.getCreatedAt());

        var retrievedAppointment =
                appointmentRepository.findById(savedAppointment.getId());

        assertTrue(retrievedAppointment.isPresent());

        var retrieved = retrievedAppointment.get();

        assertEquals("COCKER_SPANIEL", retrieved.getBreed().getCode());
        assertEquals(0, new BigDecimal("12.5").compareTo(retrieved.getWeightKg()));
        assertEquals(CoatLength.LONG, retrieved.getCoatLength());
        assertEquals(CoatTexture.WAVY, retrieved.getCoatTexture());
        assertEquals(CoatStructure.SINGLE, retrieved.getCoatStructure());
        assertEquals(MattingSeverity.MODERATE, retrieved.getMattingSeverity());
        assertEquals(Behaviour.GOOD, retrieved.getBehaviour());
        assertEquals(Service.FULL_GROOM_AND_CLIP, retrieved.getService());

        assertEquals(
                0,
                new BigDecimal("2.0").compareTo(retrieved.getGroomerExperienceYears())
        );

        assertEquals("RULES_V1", retrieved.getEstimatorVersion());
        assertEquals(95, retrieved.getEstimatedMinutes());
        assertEquals(80, retrieved.getLowerBoundMinutes());
        assertEquals(110, retrieved.getUpperBoundMinutes());

        assertEquals(100, retrieved.getActualDurationMinutes());
        assertNotNull(retrieved.getCompletedAt());
    }

    @Test
    @Transactional
    void shouldFindCompletedAppointments() {

        var breed = breedRepository.findById(1L);

        assertTrue(breed.isPresent());

        Appointment appointment = new Appointment(
                breed.get(),
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

        Appointment savedAppointment = appointmentRepository.save(appointment);

        appointment.complete(100);
        appointmentRepository.save(appointment);

        var completedAppointments =
                appointmentRepository.findByActualDurationMinutesIsNotNull();

        assertTrue(
                completedAppointments.stream()
                        .anyMatch(a -> a.getId().equals(savedAppointment.getId()))
        );

        var completedAppointment = completedAppointments.stream()
                .filter(a -> a.getId().equals(savedAppointment.getId()))
                .findFirst()
                .orElseThrow();

        assertEquals(100, completedAppointment.getActualDurationMinutes());
        assertNotNull(completedAppointment.getCompletedAt());
    }

    @Test
    @Transactional
    void shouldFindCompletedAppointmentsForMl() {

        var breed = breedRepository.findById(1L);

        assertTrue(breed.isPresent());

        Appointment completedAppointment = new Appointment(
                breed.get(),
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

        Appointment incompleteAppointment = new Appointment(
                breed.get(),
                new BigDecimal("8.0"),
                CoatLength.MEDIUM,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.VERY_GOOD,
                Service.BATH_AND_DRY,
                new BigDecimal("5.0"),
                "RULES_V1",
                45,
                38,
                52
        );

        appointmentRepository.save(completedAppointment);
        appointmentRepository.save(incompleteAppointment);

        completedAppointment.complete(100);
        appointmentRepository.save(completedAppointment);

        var results = appointmentRepository.findCompletedAppointmentsForMl();

        assertTrue(
                results.stream()
                        .anyMatch(result ->
                                result.getBreed().equals("COCKER_SPANIEL")
                                        && result.getWeightKg().compareTo(new BigDecimal("12.5")) == 0
                                        && result.getCoatLength().equals("LONG")
                                        && result.getCoatTexture().equals("WAVY")
                                        && result.getCoatStructure().equals("SINGLE")
                                        && result.getMattingSeverity().equals("MODERATE")
                                        && result.getBehaviour().equals("GOOD")
                                        && result.getService().equals("FULL_GROOM_AND_CLIP")
                                        && result.getGroomerExperienceYears().compareTo(new BigDecimal("2.0")) == 0
                                        && result.getActualDurationMinutes() == 100
                        )
        );

        assertFalse(
                results.stream()
                        .anyMatch(result ->
                                result.getWeightKg().compareTo(new BigDecimal("8.0")) == 0
                        )
        );
    }
}