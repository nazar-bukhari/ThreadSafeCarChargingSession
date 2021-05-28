package io.everon.assignment.dao;

import io.everon.assignment.model.ChargingSession;
import io.everon.assignment.model.ChargingSessionResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IChargingSessionRepository {

    ChargingSessionResponse saveChargingSession(ChargingSession request);
    List<ChargingSession> getAllChargingSessions();
    Optional<ChargingSession> getChargingSessionById(UUID id);
}
