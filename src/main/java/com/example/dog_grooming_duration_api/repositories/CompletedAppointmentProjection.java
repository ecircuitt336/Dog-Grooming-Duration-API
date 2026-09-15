package com.example.dog_grooming_duration_api.repositories;

import java.math.BigDecimal;

public interface CompletedAppointmentProjection {

    String getBreed();

    BigDecimal getWeightKg();

    String getCoatLength();

    String getCoatTexture();

    String getCoatStructure();

    String getMattingSeverity();

    String getBehaviour();

    String getService();

    BigDecimal getGroomerExperienceYears();

    Integer getActualDurationMinutes();
}