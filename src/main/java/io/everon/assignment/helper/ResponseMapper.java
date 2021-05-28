package io.everon.assignment.helper;

import io.everon.assignment.model.ChargingSession;
import io.everon.assignment.model.ChargingSessionResponse;

public class ResponseMapper {

    /***
     * Map chargingSessionRequest to ChargingSessionResponse
     * @param chargingSessionRequest Contains UUID, Station ID, Started AT, Stopped AT and Status
     * @return ChargingSessionResponse
     */
    public static ChargingSessionResponse ChargingSessionResponseMapper(ChargingSession chargingSessionRequest){

        return new ChargingSessionResponse(chargingSessionRequest.getId(),
                                            chargingSessionRequest.getStationId(),
                                            chargingSessionRequest.getStartedAt(),
                                            chargingSessionRequest.getStatus());
    }
}
