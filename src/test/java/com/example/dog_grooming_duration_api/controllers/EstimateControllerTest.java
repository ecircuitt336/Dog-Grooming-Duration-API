package com.example.dog_grooming_duration_api.controllers;

import com.example.dog_grooming_duration_api.services.EstimateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(EstimateController.class)
class EstimateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstimateService estimateService;

    @Test
    void shouldAcceptValidEstimateRequest() throws Exception {
        String requestJson = """
                {
                    "breed": "COCKER_SPANIEL",
                    "weightKg": 12.5,
                    "coatLength": "LONG",
                    "coatTexture": "WAVY",
                    "coatStructure": "SINGLE",
                    "mattingSeverity": "MODERATE",
                    "behaviour": "GOOD",
                    "service": "FULL_GROOM_AND_CLIP",
                    "groomerExperienceYears": 2.0
                }
                """;

        mockMvc.perform(
                post("/api/v1/estimates")
                        .contentType("application/json")
                        .content(requestJson)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void shouldRejectInvalidEstimateRequest() throws Exception {
        String requestJson = """
            {
                "breed": "COCKER_SPANIEL",
                "weightKg": 0,
                "coatLength": "LONG",
                "coatTexture": "WAVY",
                "coatStructure": "SINGLE",
                "mattingSeverity": "MODERATE",
                "behaviour": "GOOD",
                "service": "FULL_GROOM_AND_CLIP",
                "groomerExperienceYears": 2.0
            }
            """;

        mockMvc.perform(
                post("/api/v1/estimates")
                        .contentType("application/json")
                        .content(requestJson)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("message").value("Request validation failed"))
                .andExpect(jsonPath("fieldErrors.weightKg").value("must be greater than 0.0"));
    }
}