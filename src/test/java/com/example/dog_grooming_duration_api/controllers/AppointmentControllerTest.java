package com.example.dog_grooming_duration_api.controllers;

import com.example.dog_grooming_duration_api.services.EstimateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstimateService estimateService;

    @Test
    void shouldAcceptValidActualDurationRequest() throws Exception {

        UUID appointmentId = UUID.randomUUID();

        String requestJson = """
                {
                    "actualDurationMinutes": 103
                }
                """;

        mockMvc.perform(
                        post("/api/v1/appointments/" + appointmentId + "/actual-duration")
                                .contentType("application/json")
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(estimateService).completeAppointment(appointmentId, 103);
    }

    @Test
    void shouldRejectInvalidActualDurationRequest() throws Exception {

        UUID appointmentId = UUID.randomUUID();

        String requestJson = """
            {
                "actualDurationMinutes": 0
            }
            """;

        mockMvc.perform(
                        post("/api/v1/appointments/" + appointmentId + "/actual-duration")
                                .contentType("application/json")
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());

        verify(estimateService, never())
                .completeAppointment(appointmentId, 0);
    }

    @Test
    void shouldRejectInvalidAppointmentId() throws Exception {

        String requestJson = """
            {
                "actualDurationMinutes": 103
            }
            """;

        mockMvc.perform(
                        post("/api/v1/appointments/not-a-uuid/actual-duration")
                                .contentType("application/json")
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(estimateService);
    }
}