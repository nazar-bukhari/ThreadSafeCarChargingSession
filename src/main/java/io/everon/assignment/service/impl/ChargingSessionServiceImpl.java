package io.everon.assignment.service.impl;

import io.everon.assignment.dao.IChargingSessionRepository;
import io.everon.assignment.exception.InvalidRequestException;
import io.everon.assignment.exception.SessionNotFoundException;
import io.everon.assignment.model.*;
import io.everon.assignment.service.IChargingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component(value = "chargingSessionServiceImpl")
@Service
public class ChargingSessionServiceImpl implements IChargingSessionService {

    @Autowired
    private IChargingSessionRepository chargingRepository;

    /***
     * This method is used to save charging session
     * @param request ChargingSessionRequest
     * @return ChargingSessionResponse
     */
    @Override
    public ChargingSessionResponse saveChargingSession(ChargingSessionRequest request) {
        Objects.requireNonNull(request, "request can not be null");
        return chargingRepository.saveChargingSession(buildChargingSession(request.getStationId()));
    }

    /***
     * This method is used to get all the saved charging sessions
     * @return List of ChargingSession
     */
    @Override
    public List<ChargingSession> getAllChargingSessions() {
        return new ArrayList<>(chargingRepository.getAllChargingSessions());
    }

    /***
     * This method is used to stop an IN_PROGRESS charging session
     * @param id UUID
     * @return ChargingSession
     */
    @Override
    public ChargingSession stopChargingSession(String id) {

        if(id == null || id.length() == 0) throw new InvalidRequestException("Invalid id provided");

        UUID uuid = UUID.fromString(id);
        ChargingSession chargingSession = chargingRepository.getChargingSessionById(uuid)
                .filter(f -> f.getStatus().equals(StatusEnum.IN_PROGRESS))
                .orElseThrow(() -> new SessionNotFoundException("No Active Session Found for id: "+id));

        chargingSession.setStatus(StatusEnum.FINISHED);
        chargingSession.setStoppedAt(LocalDateTime.now());

        chargingRepository.saveChargingSession(chargingSession);

        return chargingSession;
    }

    /***
     * This method generate charging session summary of last one minute
     * @return ChargingSessionSummary
     */
    @Override
    public ChargingSessionSummary getChargingSessionSummaryReport() {

        ChargingSessionSummary chargingSessionSummary = new ChargingSessionSummary();

        List<ChargingSession> lastMinuteStartedChargingSessionList = chargingRepository.getAllChargingSessions().
                                                    stream().
                                                    filter(f -> f.getStartedAt().isAfter(LocalDateTime.now().minus(1, ChronoUnit.MINUTES))).
                                                    collect(Collectors.toList());

        List<ChargingSession> lastMinuteStoppedChargingSessionList = chargingRepository.getAllChargingSessions().
                                                    stream().
                                                    filter(f -> f.getStatus().equals(StatusEnum.FINISHED) && f.getStoppedAt().isAfter(LocalDateTime.now().minus(1, ChronoUnit.MINUTES))).
                                                    collect(Collectors.toList());

        chargingSessionSummary.setStartedCount(lastMinuteStartedChargingSessionList.size());
        chargingSessionSummary.setStoppedCount(lastMinuteStoppedChargingSessionList.size());
        chargingSessionSummary.setTotalCount(chargingSessionSummary.getStartedCount() + chargingSessionSummary.getStoppedCount());

        return chargingSessionSummary;
    }

    private ChargingSession buildChargingSession(String stationId){

        if(stationId == null || stationId.isEmpty() || stationId.equals(" ")) throw new InvalidRequestException("No Or Invalid Station Id Provided");

        final LocalDateTime startedAt = LocalDateTime.now();
        final UUID uuid = UUID.randomUUID();

        final ChargingSession chargingSession = new ChargingSession();
        chargingSession.setId(uuid);
        chargingSession.setStartedAt(startedAt);
        chargingSession.setStationId(stationId);
        chargingSession.setStatus(StatusEnum.IN_PROGRESS);

        return chargingSession;
    }

}
