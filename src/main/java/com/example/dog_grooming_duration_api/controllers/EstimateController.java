package com.example.dog_grooming_duration_api.controllers;

import com.example.dog_grooming_duration_api.dtos.EstimateRequest;
import com.example.dog_grooming_duration_api.dtos.EstimateResponse;
import com.example.dog_grooming_duration_api.services.EstimateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for estimated the duration of an appointment.
 */

// For details on RequestMapping, Postmapping, Valid, and RequestBody, see AppointmentController.java

@RestController
@RequestMapping("/api/v1/estimates")
public class EstimateController {
    private final EstimateService estimateService;

    public EstimateController(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    // POST
    @PostMapping
    public EstimateResponse createEstimate(@Valid @RequestBody EstimateRequest request) {
        return estimateService.createEstimate(request);
    }
}