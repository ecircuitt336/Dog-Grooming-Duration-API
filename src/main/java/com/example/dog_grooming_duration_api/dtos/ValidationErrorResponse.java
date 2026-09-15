package com.example.dog_grooming_duration_api.dtos;

import java.util.Map;

public class ValidationErrorResponse {

    private final String error;
    private final String message;
    private final Map<String, String> fieldErrors;

    public ValidationErrorResponse(
            String error,
            String message,
            Map<String, String> fieldErrors) {
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}