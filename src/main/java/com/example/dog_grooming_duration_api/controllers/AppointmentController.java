package com.example.dog_grooming_duration_api.controllers;

import com.example.dog_grooming_duration_api.dtos.ActualDurationRequest;
import com.example.dog_grooming_duration_api.services.EstimateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for completing an appointment
 */

// RestController tells Spring that this class handles HTTP requests and returns HTTP responses.
@RestController
// RequestMapping establishes a base URL for each endpoint in this controller
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final EstimateService estimateService;

    public AppointmentController(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    // This method is available at endpoint POST "/api/v1/appointments/{id}/actual-duration"
    @PostMapping("/{id}/actual-duration")
    // PathVariable extracts the UUID id.
    // Valid runs the validation rules defined on ActualDurationRequest.
    // RequestBody converts the JSON body of the HTTP request into an ActualDurationRequest object.
    public void completeAppointment(@PathVariable UUID id, @Valid @RequestBody ActualDurationRequest request) {
        /**
         * This method gives the ID and actual duration to the service to be completed.
         */
        estimateService.completeAppointment(id, request.getActualDurationMinutes());
    }
}