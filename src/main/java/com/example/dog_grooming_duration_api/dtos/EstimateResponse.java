package com.example.dog_grooming_duration_api.dtos;

/**
 * This class represents a response from the EstimateService when estimating how long a dog groom will take.
 */

public class EstimateResponse {
    private Integer estimatedMinutes;
    private Integer lowerBoundMinutes;
    private Integer upperBoundMinutes;

    public EstimateResponse(Integer estimatedMinutes, Integer lowerBoundMinutes, Integer upperBoundMinutes) {
        this.estimatedMinutes = estimatedMinutes;
        this.lowerBoundMinutes = lowerBoundMinutes;
        this.upperBoundMinutes = upperBoundMinutes;
    }

    // Getters

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public Integer getLowerBoundMinutes() {
        return lowerBoundMinutes;
    }

    public Integer getUpperBoundMinutes() {
        return upperBoundMinutes;
    }
}
