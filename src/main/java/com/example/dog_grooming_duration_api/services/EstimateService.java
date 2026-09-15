package com.example.dog_grooming_duration_api.services;

import com.example.dog_grooming_duration_api.dtos.EstimateRequest;
import com.example.dog_grooming_duration_api.dtos.EstimateResponse;
import com.example.dog_grooming_duration_api.entities.Appointment;
import com.example.dog_grooming_duration_api.entities.Breed;
import com.example.dog_grooming_duration_api.repositories.AppointmentRepository;
import com.example.dog_grooming_duration_api.repositories.BreedRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));

        appointment.complete(actualDurationMinutes);

        appointmentRepository.save(appointment);
    }
}
