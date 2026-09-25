package com.example.dog_grooming_duration_api.services;

import com.example.dog_grooming_duration_api.repositories.AppointmentRepository;
import com.example.dog_grooming_duration_api.repositories.CompletedAppointmentProjection;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.nio.file.Files;

/**
 * This class is responsible for exporting completed appointment data into a CSV file for the ML pipeline.
 */

@Service
public class MlDatasetExportService {

    private final AppointmentRepository appointmentRepository;
    private final MlDatasetCsvWriter csvWriter;

    public MlDatasetExportService(AppointmentRepository appointmentRepository, MlDatasetCsvWriter csvWriter) {
        this.appointmentRepository = appointmentRepository;
        this.csvWriter = csvWriter;
    }

    public List<CompletedAppointmentProjection> getCompletedAppointments() {
        return appointmentRepository.findCompletedAppointmentsForMl();
    }

    public void exportToCsv(Path outputPath) throws IOException {
        /**
         * Exports the CSV to a specific Path.
         */
        var appointments = getCompletedAppointments();
        var csv = csvWriter.write(appointments);

        Files.writeString(outputPath, csv);
    }

    public void exportCompletedAppointments() throws IOException {
        exportToCsv(Path.of("data", "ml", "completed_appointments.csv"));
    }
}