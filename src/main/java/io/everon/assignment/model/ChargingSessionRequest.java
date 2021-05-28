package io.everon.assignment.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChargingSessionRequest {

    @NotNull
    @NotBlank
    private String stationId;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
