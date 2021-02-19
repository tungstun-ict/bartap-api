package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.SessionService;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.presentation.dto.response.SessionResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("bars/{barId}/sessions")
@RolesAllowed("ROLE_BAR_OWNER")
public class SessionController {
    private final SessionService SESSION_SERVICE;
    private final ResponseMapper RESPONSE_MAPPER;

    public SessionController(SessionService sessionService, ResponseMapper responseMapper) {
        this.SESSION_SERVICE = sessionService;
        this.RESPONSE_MAPPER = responseMapper;
    }

    private SessionResponse convertToSessionResult(Session session){
        return RESPONSE_MAPPER.convert(session, SessionResponse.class);
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> getAllBarSessions(
            @PathVariable("barId") Long barId)
            throws NotFoundException {

        List<Session> allSessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        List<SessionResponse> sessionResponses = RESPONSE_MAPPER.convertList(allSessions, SessionResponse.class);
        return new ResponseEntity<>(sessionResponses,  HttpStatus.OK);
    }

    @GetMapping(path = "/{sessionId}")
    public ResponseEntity<SessionResponse> getBarSessionsById(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        return new ResponseEntity<>(convertToSessionResult(session),  HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SessionResponse> createNewSession(
            @PathVariable("barId") Long barId)
            throws NotFoundException {

        Session session = this.SESSION_SERVICE.createNewSession(barId);
        return new ResponseEntity<>(convertToSessionResult(session),  HttpStatus.CREATED);
    }

    @PatchMapping("/{sessionId}/end")
    public ResponseEntity<SessionResponse> endSession(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId
    ) throws NotFoundException {
        Session session = this.SESSION_SERVICE.endSession(barId, sessionId);
        return new ResponseEntity<>(convertToSessionResult(session),  HttpStatus.OK);
    }

    @PatchMapping("/{sessionId}/bartenders/{bartenderId}/add")
    public ResponseEntity<SessionResponse> addBartenderToSession(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("bartenderId") Long bartenderId
    ) throws NotFoundException {
        Session session = this.SESSION_SERVICE.addBartenderToSession(barId, sessionId, bartenderId);
        return new ResponseEntity<>(convertToSessionResult(session),  HttpStatus.OK);
    }

    @PatchMapping("/{sessionId}/bartenders/{bartenderId}/remove")
    public ResponseEntity<SessionResponse> removeBartenderFromSession(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("bartenderId") Long bartenderId
    ) throws NotFoundException {
        Session session = this.SESSION_SERVICE.removeBartenderFromSession(barId, sessionId, bartenderId);
        return new ResponseEntity<>(convertToSessionResult(session),  HttpStatus.OK);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId)
            throws NotFoundException {

        this.SESSION_SERVICE.deleteSession(barId, sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
