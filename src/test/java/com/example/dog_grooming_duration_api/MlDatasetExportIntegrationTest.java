package com.example.dog_grooming_duration_api;

import com.example.dog_grooming_duration_api.services.MlDatasetExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MlDatasetExportIntegrationTest {

    @Autowired
    private MlDatasetExportService mlDatasetExportService;

    @Test
    void shouldExportCompletedAppointmentsFromDatabase() throws Exception {

        Path outputPath = Path.of(
                "data",
                "ml",
                "completed_appointments.csv"
        );

        try {
            mlDatasetExportService.exportCompletedAppointments();

            assertTrue(Files.exists(outputPath));

            String csv = Files.readString(outputPath);

            assertTrue(csv.contains(
                    "breed,weight_kg,coat_length,coat_texture,coat_structure,"
                            + "matting_severity,behaviour,service,"
                            + "groomer_experience_years,actual_duration_minutes"
            ));

            assertTrue(csv.contains(
                    "COCKER_SPANIEL,12.50,LONG,WAVY,SINGLE,MODERATE,GOOD,"
                            + "FULL_GROOM_AND_CLIP,2.0,100"
            ));

        } finally {
            Files.deleteIfExists(outputPath);
        }
    }
}