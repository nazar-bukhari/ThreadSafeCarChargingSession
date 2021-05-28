package io.everon.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.everon.assignment.model.*;
import io.everon.assignment.service.IChargingSessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ChargingSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private IChargingSessionService chargingSessionService;

    private static final String Station_ID = "Test-123";

    @Test
    @DisplayName("/POST ChargingSession should return 200")
    public void saveSession() throws Exception {

        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest();
        chargingSessionRequest.setStationId(Station_ID);

        ChargingSessionResponse expectedResponse = new ChargingSessionResponse(UUID.randomUUID(), Station_ID, LocalDateTime.now(), StatusEnum.IN_PROGRESS);

        Mockito.doReturn(expectedResponse).when(chargingSessionService).saveChargingSession(chargingSessionRequest);

        mockMvc.perform(post("/chargingSessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(chargingSessionRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/POST ChargingSession with null/empty request should return bad request")
    public void saveSessionWithBadRequest() throws Exception {

        mockMvc.perform(post("/chargingSessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("/GET ChargingSessions should return list of charging sessions")
    public void getSession() throws Exception{

        doReturn(emptyList()).when(chargingSessionService).getAllChargingSessions();

        mockMvc.perform(get("/chargingSessions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", empty()));

    }

    @Test
    @DisplayName("/PUT ChargingSession should stop the charging session")
    public void stopSession() throws Exception{

        ChargingSession expectedSessionResponse = new ChargingSession();
        expectedSessionResponse.setId(UUID.randomUUID());
        expectedSessionResponse.setStatus(StatusEnum.FINISHED);
        expectedSessionResponse.setStoppedAt(LocalDateTime.now());

        doReturn(expectedSessionResponse).when(chargingSessionService).stopChargingSession(anyString());

        mockMvc.perform(put("/chargingSessions/{id}", expectedSessionResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stoppedAt", equalTo(expectedSessionResponse.getStoppedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(expectedSessionResponse.getStatus().toString())));

    }

    @Test
    @DisplayName("/GET ChargingSession Summary")
    public void getChargingSessionSummary() throws Exception {

        ChargingSessionSummary summaryResponse = new ChargingSessionSummary();
        summaryResponse.setTotalCount(2);
        summaryResponse.setStoppedCount(1);
        summaryResponse.setStartedCount(1);

        doReturn(summaryResponse).when(chargingSessionService).getChargingSessionSummaryReport();

        mockMvc.perform(get("/chargingSessions/summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalCount", equalTo(2)))
                .andExpect(jsonPath("$.startedCount", equalTo(1)))
                .andExpect(jsonPath("$.stoppedCount", equalTo(1)));
    }
}
