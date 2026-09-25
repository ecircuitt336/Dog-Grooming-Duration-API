package com.example.dog_grooming_duration_api.services;

import com.example.dog_grooming_duration_api.dtos.EstimateRequest;
import com.example.dog_grooming_duration_api.dtos.EstimateResponse;
import com.example.dog_grooming_duration_api.entities.Appointment;
import com.example.dog_grooming_duration_api.entities.Breed;
import com.example.dog_grooming_duration_api.repositories.AppointmentRepository;
import com.example.dog_grooming_duration_api.repositories.BreedRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This class is responsible for setting out the steps required to create a grooming-duration estimate and record the actual duration once the appointment is completed.
 */

// Service means that this class is a service component.
// It is similar to @Component but this represents a bean as well as service-layer logic.
@Service
public class EstimateService {

    private final RulesBasedEstimator estimator;
    private final BreedRepository breedRepository;
    private final AppointmentRepository appointmentRepository;

    public EstimateService(RulesBasedEstimator estimator, BreedRepository breedRepository, AppointmentRepository appointmentRepository) {
        this.estimator = estimator;
        this.breedRepository = breedRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public EstimateResponse createEstimate(EstimateRequest request) {
        /**
         * Uses the RulesBasedEstimator to create an initial estimate and stores the appointment in the database.
         */
        Breed breed = breedRepository.findByCode(request.getBreed())
                .orElseThrow(() -> new IllegalArgumentException("Unknown breed: " + request.getBreed()));

        EstimationResult result = estimator.estimate(
                breed,
                request.getWeightKg(),
                request.getCoatLength(),
                request.getCoatTexture(),
                request.getCoatStructure(),
                request.getMattingSeverity(),
                request.getBehaviour(),
                request.getService(),
                request.getGroomerExperienceYears()
        );

        Appointment appointment = new Appointment(
                breed,
                request.getWeightKg(),
                request.getCoatLength(),
                request.getCoatTexture(),
                request.getCoatStructure(),
                request.getMattingSeverity(),
                request.getBehaviour(),
                request.getService(),
                request.getGroomerExperienceYears(),
                "RULES_V1",
                result.getEstimatedMinutes(),
                result.getLowerBoundMinutes(),
                result.getUpperBoundMinutes()
        );

        appointmentRepository.save(appointment);

        return new EstimateResponse(result.getEstimatedMinutes(), result.getLowerBoundMinutes(), result.getUpperBoundMinutes());
    }

    public void completeAppointment(UUID appointmentId, int actualDurationMinutes) {
        /**
         * Marks an appointment as complete and updates it in the database.
         */
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));

        appointment.complete(actualDurationMinutes);

        appointmentRepository.save(appointment);
    }
}
