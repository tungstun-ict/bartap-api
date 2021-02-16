package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.SessionService;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.presentation.dto.response.SessionResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bars/{barId}/sessions")
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

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId)
            throws NotFoundException {

        this.SESSION_SERVICE.deleteSession(barId, sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
