package com.example.dog_grooming_duration_api.controllers;

import com.example.dog_grooming_duration_api.dtos.EstimateRequest;
import com.example.dog_grooming_duration_api.dtos.EstimateResponse;
import com.example.dog_grooming_duration_api.services.EstimateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

// Tells Spring that this class handles HTTP requests and the responses are REST responses.
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