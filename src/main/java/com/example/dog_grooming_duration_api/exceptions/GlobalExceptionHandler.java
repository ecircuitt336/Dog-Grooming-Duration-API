package com.example.dog_grooming_duration_api.exceptions;

import com.example.dog_grooming_duration_api.dtos.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is the global error handler for validation errors.
 * It catches validation failures from controllers and turns them into the ValidationErrorResponse DTO.
 */

// RestControllerAdvice means this class contains exception-handling logic that applies across all REST controllers.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // This method is called when a MethodArgumentNotValidException occurs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // ResponseStatus tells Spring to return HTTP 400 Bad Request
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException exception) {
        var fieldErrors = exception.getBindingResult().getFieldErrors();

        Map<String, String> errors = fieldErrors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        return new ValidationErrorResponse("VALIDATION_ERROR", "Request validation failed", errors);
    }
}
