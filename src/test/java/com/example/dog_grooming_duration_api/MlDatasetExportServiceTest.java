package com.example.dog_grooming_duration_api;

import com.example.dog_grooming_duration_api.repositories.AppointmentRepository;
import com.example.dog_grooming_duration_api.repositories.CompletedAppointmentProjection;
import com.example.dog_grooming_duration_api.services.MlDatasetCsvWriter;
import com.example.dog_grooming_duration_api.services.MlDatasetExportService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MlDatasetExportServiceTest {

    @Test
    void shouldReturnCompletedAppointmentsForMl() {

        AppointmentRepository appointmentRepository =
                mock(AppointmentRepository.class);

        MlDatasetCsvWriter csvWriter =
                mock(MlDatasetCsvWriter.class);

        MlDatasetExportService service =
                new MlDatasetExportService(appointmentRepository, csvWriter);

        CompletedAppointmentProjection projection =
                mock(CompletedAppointmentProjection.class);

        List<CompletedAppointmentProjection> expected =
                List.of(projection);

        when(appointmentRepository.findCompletedAppointmentsForMl())
                .thenReturn(expected);

        var result = service.getCompletedAppointments();

        assertSame(expected, result);

        verify(appointmentRepository).findCompletedAppointmentsForMl();
    }

    @Test
    void shouldExportCompletedAppointmentsToCsv() throws IOException {

        AppointmentRepository appointmentRepository =
                mock(AppointmentRepository.class);

        MlDatasetCsvWriter csvWriter =
                mock(MlDatasetCsvWriter.class);

        MlDatasetExportService service =
                new MlDatasetExportService(appointmentRepository, csvWriter);

        CompletedAppointmentProjection projection =
                mock(CompletedAppointmentProjection.class);

        List<CompletedAppointmentProjection> appointments =
                List.of(projection);

        String csv = "breed,weight_kg,...\nCOCKER_SPANIEL,12.50,...\n";

        Path outputPath = Files.createTempFile(
                "completed-appointments",
                ".csv"
        );

        when(appointmentRepository.findCompletedAppointmentsForMl())
                .thenReturn(appointments);

        when(csvWriter.write(appointments))
                .thenReturn(csv);

        service.exportToCsv(outputPath);

        assertEquals(csv, Files.readString(outputPath));

        verify(appointmentRepository).findCompletedAppointmentsForMl();
        verify(csvWriter).write(appointments);

        Files.deleteIfExists(outputPath);
    }

    @Test
    void shouldExportCompletedAppointmentsToDefaultCsvLocation() throws IOException {

        AppointmentRepository appointmentRepository =
                mock(AppointmentRepository.class);

        MlDatasetCsvWriter csvWriter =
                mock(MlDatasetCsvWriter.class);

        MlDatasetExportService service =
                new MlDatasetExportService(appointmentRepository, csvWriter);

        CompletedAppointmentProjection projection =
                mock(CompletedAppointmentProjection.class);

        List<CompletedAppointmentProjection> appointments =
                List.of(projection);

        String csv = "breed,weight_kg,...\nCOCKER_SPANIEL,12.50,...\n";

        when(appointmentRepository.findCompletedAppointmentsForMl())
                .thenReturn(appointments);

        when(csvWriter.write(appointments))
                .thenReturn(csv);

        Path outputPath = Path.of(
                "data",
                "ml",
                "completed_appointments.csv"
        );

        try {
            service.exportCompletedAppointments();

            assertTrue(Files.exists(outputPath));
            assertEquals(csv, Files.readString(outputPath));

            verify(appointmentRepository).findCompletedAppointmentsForMl();
            verify(csvWriter).write(appointments);

        } finally {
            Files.deleteIfExists(outputPath);
        }
    }
}