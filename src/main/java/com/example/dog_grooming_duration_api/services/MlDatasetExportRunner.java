package com.example.dog_grooming_duration_api.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MlDatasetExportRunner implements CommandLineRunner {

    private final MlDatasetExportService exportService;

    public MlDatasetExportRunner(MlDatasetExportService exportService) {
        this.exportService = exportService;
    }

    @Override
    public void run(String... args) throws Exception {
        for (String arg : args) {
            if (arg.equals("--export-ml-dataset")) {
                exportService.exportCompletedAppointments();
                System.out.println("ML dataset exported successfully.");
            }
        }
    }
}