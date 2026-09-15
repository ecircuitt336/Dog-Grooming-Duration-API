package com.example.dog_grooming_duration_api.dtos;

import com.example.dog_grooming_duration_api.entities.Breed;
import com.example.dog_grooming_duration_api.repositories.AppointmentRepository;
import com.example.dog_grooming_duration_api.repositories.BreedRepository;
import com.example.dog_grooming_duration_api.services.EstimateService;
import com.example.dog_grooming_duration_api.services.RulesBasedEstimator;
import com.example.dog_grooming_duration_api.enums.*;
import com.example.dog_grooming_duration_api.services.EstimationResult;
import com.example.dog_grooming_duration_api.entities.Appointment;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EstimateServiceTest {

    private final RulesBasedEstimator estimator = mock(RulesBasedEstimator.class);

    private final BreedRepository breedRepository = mock(BreedRepository.class);

    private final AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);

    private final EstimateService estimateService =
            new EstimateService(estimator, breedRepository, appointmentRepository);

    @Test
    void knownBreedIsResolvedAndRequestIsPassedToEstimator() {

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

        Breed breed = mock(Breed.class);

        when(breedRepository.findByCode("COCKER_SPANIEL"))
                .thenReturn(Optional.of(breed));

        EstimationResult estimationResult =
                new EstimationResult(95, 80, 110);

        when(estimator.estimate(
                eq(breed),
                eq(new BigDecimal("12.5")),
                eq(CoatLength.LONG),
                eq(CoatTexture.WAVY),
                eq(CoatStructure.SINGLE),
                eq(MattingSeverity.MODERATE),
                eq(Behaviour.GOOD),
                eq(Service.FULL_GROOM_AND_CLIP),
                eq(new BigDecimal("2.0"))
        )).thenReturn(estimationResult);

        EstimateResponse response = estimateService.createEstimate(request);

        assertEquals(95, response.getEstimatedMinutes());
        assertEquals(80, response.getLowerBoundMinutes());
        assertEquals(110, response.getUpperBoundMinutes());

        verify(breedRepository).findByCode("COCKER_SPANIEL");

        verify(estimator).estimate(
                eq(breed),
                eq(new BigDecimal("12.5")),
                eq(CoatLength.LONG),
                eq(CoatTexture.WAVY),
                eq(CoatStructure.SINGLE),
                eq(MattingSeverity.MODERATE),
                eq(Behaviour.GOOD),
                eq(Service.FULL_GROOM_AND_CLIP),
                eq(new BigDecimal("2.0"))
        );

        ArgumentCaptor<Appointment> appointmentCaptor =
                ArgumentCaptor.forClass(Appointment.class);

        verify(appointmentRepository).save(appointmentCaptor.capture());

        Appointment savedAppointment = appointmentCaptor.getValue();

        assertEquals(breed, savedAppointment.getBreed());
        assertEquals(request.getWeightKg(), savedAppointment.getWeightKg());
        assertEquals(request.getCoatLength(), savedAppointment.getCoatLength());
        assertEquals(request.getCoatTexture(), savedAppointment.getCoatTexture());
        assertEquals(request.getCoatStructure(), savedAppointment.getCoatStructure());
        assertEquals(request.getMattingSeverity(), savedAppointment.getMattingSeverity());
        assertEquals(request.getBehaviour(), savedAppointment.getBehaviour());
        assertEquals(request.getService(), savedAppointment.getService());
        assertEquals(request.getGroomerExperienceYears(),
                savedAppointment.getGroomerExperienceYears());

        assertEquals("RULES_V1", savedAppointment.getEstimatorVersion());
        assertEquals(95, savedAppointment.getEstimatedMinutes());
        assertEquals(80, savedAppointment.getLowerBoundMinutes());
        assertEquals(110, savedAppointment.getUpperBoundMinutes());
    }

    @Test
    void unknownBreedDoesNotCreateAppointment() {

        EstimateRequest request = new EstimateRequest();

        request.setBreed("UNKNOWN_BREED");
        request.setWeightKg(new BigDecimal("12.5"));
        request.setCoatLength(CoatLength.LONG);
        request.setCoatTexture(CoatTexture.WAVY);
        request.setCoatStructure(CoatStructure.SINGLE);
        request.setMattingSeverity(MattingSeverity.MODERATE);
        request.setBehaviour(Behaviour.GOOD);
        request.setService(Service.FULL_GROOM_AND_CLIP);
        request.setGroomerExperienceYears(new BigDecimal("2.0"));

        when(breedRepository.findByCode("UNKNOWN_BREED"))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> estimateService.createEstimate(request)
        );

        verify(estimator, never()).estimate(any(), any(), any(), any(), any(),
                any(), any(), any(), any());

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void existingAppointmentCanBeCompleted() {

        Appointment appointment = mock(Appointment.class);

        UUID appointmentId = UUID.randomUUID();

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        estimateService.completeAppointment(appointmentId, 103);

        verify(appointmentRepository).findById(appointmentId);
        verify(appointment).complete(103);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void missingAppointmentCannotBeCompleted() {

        UUID appointmentId = UUID.randomUUID();

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> estimateService.completeAppointment(appointmentId, 103)
        );

        verify(appointmentRepository).findById(appointmentId);
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void alreadyCompletedAppointmentCannotBeCompletedAgain() {

        Appointment appointment = mock(Appointment.class);
        UUID appointmentId = UUID.randomUUID();

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        doThrow(new IllegalStateException("Appointment has already been completed."))
                .when(appointment)
                .complete(103);

        assertThrows(
                IllegalStateException.class,
                () -> estimateService.completeAppointment(appointmentId, 103)
        );

        verify(appointment).complete(103);
        verify(appointmentRepository, never()).save(any());
    }
}