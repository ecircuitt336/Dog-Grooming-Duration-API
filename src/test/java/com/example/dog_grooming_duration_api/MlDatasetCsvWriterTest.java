package com.example.dog_grooming_duration_api;

import com.example.dog_grooming_duration_api.repositories.CompletedAppointmentProjection;
import com.example.dog_grooming_duration_api.services.MlDatasetCsvWriter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MlDatasetCsvWriterTest {

    private final MlDatasetCsvWriter writer = new MlDatasetCsvWriter();

    @Test
    void shouldWriteHeaderWhenThereAreNoAppointments() {

        String result = writer.write(List.of());

        assertEquals(
                "breed,weight_kg,coat_length,coat_texture,coat_structure,"
                        + "matting_severity,behaviour,service,groomer_experience_years,"
                        + "actual_duration_minutes\n",
                result
        );
    }

    @Test
    void shouldWriteAppointmentAsCsvRow() {

        CompletedAppointmentProjection appointment =
                new CompletedAppointmentProjection() {

                    @Override
                    public String getBreed() {
                        return "COCKER_SPANIEL";
                    }

                    @Override
                    public BigDecimal getWeightKg() {
                        return new BigDecimal("12.50");
                    }

                    @Override
                    public String getCoatLength() {
                        return "LONG";
                    }

                    @Override
                    public String getCoatTexture() {
                        return "WAVY";
                    }

                    @Override
                    public String getCoatStructure() {
                        return "SINGLE";
                    }

                    @Override
                    public String getMattingSeverity() {
                        return "MODERATE";
                    }

                    @Override
                    public String getBehaviour() {
                        return "GOOD";
                    }

                    @Override
                    public String getService() {
                        return "FULL_GROOM_AND_CLIP";
                    }

                    @Override
                    public BigDecimal getGroomerExperienceYears() {
                        return new BigDecimal("2.0");
                    }

                    @Override
                    public Integer getActualDurationMinutes() {
                        return 100;
                    }
                };

        String result = writer.write(List.of(appointment));

        assertEquals(
                "breed,weight_kg,coat_length,coat_texture,coat_structure,"
                        + "matting_severity,behaviour,service,groomer_experience_years,"
                        + "actual_duration_minutes\n"
                        + "COCKER_SPANIEL,12.50,LONG,WAVY,SINGLE,MODERATE,GOOD,"
                        + "FULL_GROOM_AND_CLIP,2.0,100\n",
                result
        );
    }
}