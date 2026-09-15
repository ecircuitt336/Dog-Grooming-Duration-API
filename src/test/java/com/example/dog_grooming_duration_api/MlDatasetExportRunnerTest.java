package com.example.dog_grooming_duration_api.services;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class MlDatasetExportRunnerTest {

    @Test
    void shouldExportDatasetWhenExportArgumentIsProvided() throws Exception {
        MlDatasetExportService exportService = mock(MlDatasetExportService.class);
        MlDatasetExportRunner runner = new MlDatasetExportRunner(exportService);

        runner.run("--export-ml-dataset");

        verify(exportService).exportCompletedAppointments();
    }

    @Test
    void shouldNotExportDatasetWhenExportArgumentIsNotProvided() throws Exception {
        MlDatasetExportService exportService = mock(MlDatasetExportService.class);
        MlDatasetExportRunner runner = new MlDatasetExportRunner(exportService);

        runner.run("--some-other-argument");

        verify(exportService, never()).exportCompletedAppointments();
    }
}