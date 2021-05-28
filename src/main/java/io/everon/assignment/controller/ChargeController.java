package io.everon.assignment.controller;

import io.everon.assignment.model.ChargingSession;
import io.everon.assignment.model.ChargingSessionRequest;
import io.everon.assignment.model.ChargingSessionResponse;
import io.everon.assignment.model.ChargingSessionSummary;
import io.everon.assignment.service.IChargingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/chargingSessions")
public class ChargeController {

    @Autowired
    @Qualifier("chargingSessionServiceImpl")
    private IChargingSessionService chargingService;

    /***
     *
     * @return List of saved Charging Sessions
     */
    @GetMapping
    public ResponseEntity<List<ChargingSession>> retrieveAllChargingSessions(){
        return ResponseEntity.ok(chargingService.getAllChargingSessions());
    }

    /***
     *
     * @param chargingSessionRequest contains stationId
     * @return ChargingSessionResponse contains UUID, stationId, StartedAt and current status
     */
    @PostMapping
    public ResponseEntity<ChargingSessionResponse> submitChargingSession(@RequestBody ChargingSessionRequest chargingSessionRequest){
        return ResponseEntity.ok(chargingService.saveChargingSession(chargingSessionRequest));
    }

    /***
     *
     * @param id UUID from saved session list
     * @return ChargingSession after changing the status from IN_PROGRESS to FINISHED
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChargingSession> stopChargingSession(@PathVariable String id){
        return ResponseEntity.ok(chargingService.stopChargingSession(id));
    }

    /***
     *
     * @return Summary of all the session activity for last one minute
     */
    @GetMapping("/summary")
    public ResponseEntity<ChargingSessionSummary> retrieveChargingSessionSummary(){
        return ResponseEntity.ok(chargingService.getChargingSessionSummaryReport());
    }
}
