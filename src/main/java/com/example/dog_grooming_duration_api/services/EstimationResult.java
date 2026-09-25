package com.example.dog_grooming_duration_api.services;

/**
 * This class represents a result produced by the duration estimation logic.
 */

public class EstimationResult {

    private Integer estimatedMinutes;
    private Integer lowerBoundMinutes;
    private Integer upperBoundMinutes;

    public EstimationResult(Integer estimatedMinutes, Integer lowerBoundMinutes, Integer upperBoundMinutes) {
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
