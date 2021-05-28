package io.everon.assignment.service;

import io.everon.assignment.model.ChargingSession;
import io.everon.assignment.model.ChargingSessionRequest;
import io.everon.assignment.model.ChargingSessionResponse;
import io.everon.assignment.model.ChargingSessionSummary;

import java.util.List;
import java.util.UUID;

public interface IChargingSessionService {

    ChargingSessionResponse saveChargingSession(ChargingSessionRequest request);
    List<ChargingSession> getAllChargingSessions();
    ChargingSession stopChargingSession(String id);
    ChargingSessionSummary getChargingSessionSummaryReport();
}
