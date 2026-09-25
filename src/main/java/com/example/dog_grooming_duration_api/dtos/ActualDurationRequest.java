package com.example.dog_grooming_duration_api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * This class is a Data Transfer Object used to represent the data the client sends when recording an appointment's actual grooming duration.
 */

public class ActualDurationRequest {

    @NotNull
    @Min(1)
    private Integer actualDurationMinutes;

    public ActualDurationRequest() {
    }

    public Integer getActualDurationMinutes() {
        return actualDurationMinutes;
    }

    public void setActualDurationMinutes(Integer actualDurationMinutes) {
        this.actualDurationMinutes = actualDurationMinutes;
    }
}