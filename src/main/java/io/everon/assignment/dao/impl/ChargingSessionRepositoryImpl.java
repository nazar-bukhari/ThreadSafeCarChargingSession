package io.everon.assignment.dao.impl;

import io.everon.assignment.dao.IChargingSessionRepository;
import io.everon.assignment.helper.ResponseMapper;
import io.everon.assignment.model.ChargingSession;
import io.everon.assignment.model.ChargingSessionResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ChargingSessionRepositoryImpl implements IChargingSessionRepository {

    private ConcurrentHashMap<UUID, ChargingSession> chargingRepository;

    public ChargingSessionRepositoryImpl(){
        chargingRepository = new ConcurrentHashMap<>();
    }

    @Override
    public ChargingSessionResponse saveChargingSession(ChargingSession chargingSession) {

        chargingRepository.put(chargingSession.getId(), chargingSession);
        return ResponseMapper.ChargingSessionResponseMapper(chargingSession);
    }

    @Override
    public List<ChargingSession> getAllChargingSessions() {
        return new ArrayList<>(chargingRepository.values());
    }

    @Override
    public Optional<ChargingSession> getChargingSessionById(UUID id) {
        return Optional.ofNullable(chargingRepository.get(id));
    }
}
