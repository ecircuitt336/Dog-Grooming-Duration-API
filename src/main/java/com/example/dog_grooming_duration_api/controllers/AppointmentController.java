package com.example.dog_grooming_duration_api.controllers;

import com.example.dog_grooming_duration_api.dtos.ActualDurationRequest;
import com.example.dog_grooming_duration_api.services.EstimateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final EstimateService estimateService;

    public AppointmentController(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    @PostMapping("/{id}/actual-duration")
    public void completeAppointment(@PathVariable UUID id, @Valid @RequestBody ActualDurationRequest request) {
        estimateService.completeAppointment(id, request.getActualDurationMinutes());
    }
}