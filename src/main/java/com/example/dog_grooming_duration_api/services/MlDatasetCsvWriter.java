package com.example.dog_grooming_duration_api.services;

import com.example.dog_grooming_duration_api.repositories.CompletedAppointmentProjection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MlDatasetCsvWriter {
    private static final String HEADER = "breed,weight_kg,coat_length,coat_texture,coat_structure,"
            + "matting_severity,behaviour,service,groomer_experience_years,actual_duration_minutes";

    public String write(List<CompletedAppointmentProjection> appointments) {

        StringBuilder csv = new StringBuilder();
        csv.append(HEADER).append("\n");

        for (CompletedAppointmentProjection appointment : appointments) {
            csv.append(appointment.getBreed()).append(",");
            csv.append(appointment.getWeightKg()).append(",");
            csv.append(appointment.getCoatLength()).append(",");
            csv.append(appointment.getCoatTexture()).append(",");
            csv.append(appointment.getCoatStructure()).append(",");
            csv.append(appointment.getMattingSeverity()).append(",");
            csv.append(appointment.getBehaviour()).append(",");
            csv.append(appointment.getService()).append(",");
            csv.append(appointment.getGroomerExperienceYears()).append(",");
            csv.append(appointment.getActualDurationMinutes()).append("\n");
        }

        return csv.toString();
    }
}
