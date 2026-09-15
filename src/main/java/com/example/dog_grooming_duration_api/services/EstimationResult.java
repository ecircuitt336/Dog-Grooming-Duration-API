package com.example.dog_grooming_duration_api.services;

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
