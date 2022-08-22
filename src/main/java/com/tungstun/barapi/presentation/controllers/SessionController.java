package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.SessionService;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.presentation.dto.converter.SessionConverter;
import com.tungstun.barapi.presentation.dto.request.SessionRequest;
import com.tungstun.barapi.presentation.dto.response.ProductResponse;
import com.tungstun.barapi.presentation.dto.response.SessionResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bars/{barId}/sessions")
public class SessionController {
    private final SessionService sessionService;
    private final SessionConverter converter;

    public SessionController(SessionService sessionService, SessionConverter converter) {
        this.sessionService = sessionService;
        this.converter = converter;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds all sessions of bar",
            notes = "Provide id of bar to look up all sessions that are linked to the bar",
            response = SessionResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<SessionResponse>> getAllBarSessions(
            @ApiParam(value = "ID value for the bar you want to retrieve sessions from") @PathVariable("barId") Long barId)
            throws EntityNotFoundException {
        List<Session> allSessions = this.sessionService.getAllSessionsOfBar(barId);
        return new ResponseEntity<>(converter.convertAll(allSessions),  HttpStatus.OK);
    }

    @GetMapping(path = "/active")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds active session of bar",
            notes = "Provide id of bar and session to look up the currectly active session of the bar",
            response = SessionResponse.class
    )
    public ResponseEntity<SessionResponse> getActiveBarSessions(
            @ApiParam(value = "ID value for the bar you want to retrieve the session from") @PathVariable("barId") Long barId
    ) throws EntityNotFoundException {
        Session session = this.sessionService.getActiveSessionOfBar(barId);
        return new ResponseEntity<>(converter.convert(session),  HttpStatus.OK);
    }

    @GetMapping(path = "/{sessionId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds session of bar",
            notes = "Provide id of bar and session to look up the specific session of the bar",
            response = SessionResponse.class
    )
    public ResponseEntity<SessionResponse> getBarSessionsById(
            @ApiParam(value = "ID value for the bar you want to retrieve the session from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve") @PathVariable("sessionId") Long sessionId) throws EntityNotFoundException {
        Session session = this.sessionService.getSessionOfBar(barId, sessionId);
        return new ResponseEntity<>(converter.convert(session),  HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Creates new session for bar",
            notes = "Provide id of bar to add a new session with information from the request body to the bar",
            response = SessionResponse.class
    )
    public ResponseEntity<SessionResponse> createNewSession(
            @ApiParam(value = "ID value for the bar you want to create the session for") @PathVariable("barId") Long barId,
            @Valid @RequestBody SessionRequest sessionRequest
    ) throws EntityNotFoundException {
        Session session = this.sessionService.createNewSession(barId, sessionRequest);
        return new ResponseEntity<>(converter.convert(session),  HttpStatus.CREATED);
    }

    @PutMapping("/{sessionId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Creates new session for bar",
            notes = "Provide id of bar to update the session with information from the request body",
            response = SessionResponse.class
    )
    public ResponseEntity<SessionResponse> updateSession(
            @ApiParam(value = "ID value for the bar you want to update the session from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to update") @PathVariable("sessionId") Long sessionId,
            @Valid @RequestBody SessionRequest sessionRequest
    ) throws EntityNotFoundException {
        Session session = this.sessionService.updateSession(barId, sessionId, sessionRequest);
        return new ResponseEntity<>(converter.convert(session),  HttpStatus.CREATED);
    }

    @PatchMapping("/{sessionId}/end")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER'})")
    @ApiOperation(
            value = "Ends the session of bar",
            notes = "Provide id of bar and session to end the session of the bar",
            response = SessionResponse.class
    )
    public ResponseEntity<SessionResponse> endSession(
            @ApiParam(value = "ID value for the bar you want to end the session from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to end") @PathVariable("sessionId") Long sessionId
    ) throws EntityNotFoundException {
        Session session = this.sessionService.endSession(barId, sessionId);
        return new ResponseEntity<>(converter.convert(session),  HttpStatus.OK);
    }

    @PatchMapping("/{sessionId}/lock")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER'})")
    @ApiOperation(
            value = "Locks the session of bar",
            notes = "Provide id of bar and session to lock the session of the bar",
            response = SessionResponse.class
    )
    public ResponseEntity<SessionResponse> lockSession(
            @ApiParam(value = "ID value for the bar you want to lock the session from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to lock") @PathVariable("sessionId") Long sessionId
    ) throws EntityNotFoundException {
        Session session = this.sessionService.lockSession(barId, sessionId);
        return new ResponseEntity<>(converter.convert(session),  HttpStatus.OK);
    }

    @DeleteMapping("/{sessionId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Deletes the session of bar",
            notes = "Provide id of bar and session to delete the session from the bar",
            response = ProductResponse.class
    )
    public ResponseEntity<Void> deleteSession(
            @ApiParam(value = "ID value for the bar you want to delete the session from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to delete") @PathVariable("sessionId") Long sessionId)
            throws EntityNotFoundException {
        this.sessionService.deleteSession(barId, sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
