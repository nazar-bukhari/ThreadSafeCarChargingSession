package io.everon.assignment.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.everon.assignment.model.ChargingSessionRequest;
import io.everon.assignment.model.StatusEnum;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ChargingSessionIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String First_Station_ID = "Test-123";
    private static final String Second_Station_ID = "Test-789";

    @Test
    public void chargingSessionRestAPITest() throws Exception {

        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest();
        chargingSessionRequest.setStationId(First_Station_ID);

        //POST Session
        mockMvc.perform(MockMvcRequestBuilders.post("/chargingSessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(chargingSessionRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.stationId", equalTo(First_Station_ID)))
                .andExpect(jsonPath("$.status", equalTo(StatusEnum.IN_PROGRESS.toString())));

        ChargingSessionRequest secondChargingSessionRequest = new ChargingSessionRequest();
        secondChargingSessionRequest.setStationId(Second_Station_ID);

        mockMvc.perform(MockMvcRequestBuilders.post("/chargingSessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(secondChargingSessionRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.stationId", equalTo(Second_Station_ID)))
                .andExpect(jsonPath("$.status", equalTo(StatusEnum.IN_PROGRESS.toString())));

        //Get All Sessions
        MvcResult result = mockMvc.perform(get("/chargingSessions")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(mapper.writeValueAsString(secondChargingSessionRequest)))
                                        .andExpect(status().isOk())
                                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                                        .andExpect(jsonPath("$", hasSize(2)))
                                        .andReturn();

        String response = result.getResponse().getContentAsString();
        List<String> getUUIDList = getValueByProperty(response, "id");

        //Stropping Session
        mockMvc.perform(put("/chargingSessions/{id}", getUUIDList.get(0)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", equalTo(StatusEnum.FINISHED.toString())));

        //GET Summary
        mockMvc.perform(get("/chargingSessions/summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalCount", equalTo(3)))
                .andExpect(jsonPath("$.startedCount", equalTo(2)))
                .andExpect(jsonPath("$.stoppedCount", equalTo(1)));

    }

    public List<String> getValueByProperty(String jsonArrayStr, String key) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonArrayStr);
        return IntStream.range(0, jsonArray.length())
                .mapToObj(index -> {
                    try {
                        return ((JSONObject)jsonArray.get(index)).optString(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }
}
