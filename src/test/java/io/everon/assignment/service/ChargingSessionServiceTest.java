package io.everon.assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.everon.assignment.dao.IChargingSessionRepository;
import io.everon.assignment.exception.InvalidRequestException;
import io.everon.assignment.model.*;
import io.everon.assignment.service.impl.ChargingSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChargingSessionServiceTest {

    private static final String Station_ID = "Test-123";

    @MockBean
    private ChargingSessionServiceImpl chargingSessionService;

    @Autowired
    private ObjectMapper mapper;

    @Mock
    private IChargingSessionRepository repository;

    private ChargingSessionRequest chargingSessionRequest;

    @BeforeEach
    public void init() {
        chargingSessionRequest = new ChargingSessionRequest();
        chargingSessionRequest.setStationId(Station_ID);
    }


    @Test
    @DisplayName("Save Charging Session with provided stationId")
    public void saveChargingSession() throws JsonProcessingException {

        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest();
        chargingSessionRequest.setStationId(Station_ID);

        ChargingSessionResponse expectedResponse = new ChargingSessionResponse(UUID.randomUUID(), Station_ID, LocalDateTime.now(), StatusEnum.IN_PROGRESS);
        Mockito.doReturn(expectedResponse).when(chargingSessionService).saveChargingSession(chargingSessionRequest);

        ChargingSessionResponse actual = chargingSessionService.saveChargingSession(chargingSessionRequest);

        assertThat(mapper.writeValueAsString(actual), equalTo(mapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("Save Charging Session with null stationId")
    public void saveChargingSessionWithBadRequest(){

        chargingSessionRequest.setStationId(null);

        try {
            ChargingSessionResponse chargingSessionResponse = chargingSessionService.saveChargingSession(chargingSessionRequest);
        }
        catch (InvalidRequestException ex){
            assertEquals(ex.getMessage(), "No Or Invalid Station Id Provided");
        }
    }

    @Test
    @DisplayName("Get Charging Sessions")
    public void getChargingSessions(){

        doReturn(emptyList()).when(chargingSessionService).getAllChargingSessions();
        List<ChargingSession> actualResponse = chargingSessionService.getAllChargingSessions();

        assertEquals(0, actualResponse.size());
    }

    @Test
    @DisplayName("Stop Charging Session by UUID")
    public void stopChargingSession(){

        ChargingSession expectedSessionResponse = new ChargingSession();
        expectedSessionResponse.setId(UUID.randomUUID());
        expectedSessionResponse.setStatus(StatusEnum.FINISHED);
        expectedSessionResponse.setStoppedAt(LocalDateTime.now());

        doReturn(expectedSessionResponse).when(chargingSessionService).stopChargingSession(anyString());

        ChargingSession actualResponse = chargingSessionService.stopChargingSession("");


        assertEquals(StatusEnum.FINISHED, actualResponse.getStatus());
        assertNotNull(actualResponse.getStoppedAt());
    }

    @Test
    @DisplayName("Get Charging Sessions Summary")
    public void getSummaryChargingSessions(){

        ChargingSessionSummary summaryResponse = new ChargingSessionSummary();
        summaryResponse.setTotalCount(2);
        summaryResponse.setStoppedCount(1);
        summaryResponse.setStartedCount(1);

        doReturn(summaryResponse).when(chargingSessionService).getChargingSessionSummaryReport();

        ChargingSessionSummary actualResponse = chargingSessionService.getChargingSessionSummaryReport();

        assertEquals(2, actualResponse.getTotalCount());
        assertEquals(1, actualResponse.getStartedCount());
        assertEquals(1, actualResponse.getStoppedCount());
    }

}
