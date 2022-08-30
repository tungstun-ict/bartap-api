package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.session.SessionCommandHandler;
import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.command.CreateSession;
import com.tungstun.barapi.application.session.command.DeleteSession;
import com.tungstun.barapi.application.session.command.EndSession;
import com.tungstun.barapi.application.session.command.UpdateSession;
import com.tungstun.barapi.application.session.query.GetActiveSession;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.application.session.query.ListSessionsOfBar;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.presentation.dto.converter.SessionConverter;
import com.tungstun.barapi.presentation.dto.request.SessionRequest;
import com.tungstun.barapi.presentation.dto.response.ProductResponse;
import com.tungstun.barapi.presentation.dto.response.SessionResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bars/{barId}/sessions")
public class SessionController {
    private final SessionCommandHandler sessionCommandHandler;
    private final SessionQueryHandler sessionQueryHandler;
    private final SessionConverter converter;

    public SessionController(SessionCommandHandler sessionCommandHandler, SessionQueryHandler sessionQueryHandler, SessionConverter converter) {
        this.sessionCommandHandler = sessionCommandHandler;
        this.sessionQueryHandler = sessionQueryHandler;
        this.converter = converter;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds all sessions of bar",
            notes = "Provide categoryId of bar to look up all sessions that are linked to the bar",
            response = SessionResponse.class,
            responseContainer = "List"
    )
    public List<SessionResponse> getAllBarSessions(
            @ApiParam(value = "ID value for the bar you want to retrieve sessions from") @PathVariable("barId") UUID barId
    ) throws EntityNotFoundException {
        List<Session> allSessions = sessionQueryHandler.handle(new ListSessionsOfBar(barId));
        return converter.convertAll(allSessions);
    }

    @GetMapping(path = "/active")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds active session of bar",
            notes = "Provide categoryId of bar and session to look up the currectly active session of the bar",
            response = SessionResponse.class
    )
    public SessionResponse getActiveBarSessions(
            @ApiParam(value = "ID value for the bar you want to retrieve the session from") @PathVariable("barId") UUID barId
    ) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetActiveSession(barId));
        return converter.convert(session);
    }

    @GetMapping(path = "/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds session of bar",
            notes = "Provide categoryId of bar and session to look up the specific session of the bar",
            response = SessionResponse.class
    )
    public SessionResponse getBarSessionsById(
            @ApiParam(value = "ID value for the bar you want to retrieve the session from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to retrieve") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(barId, sessionId));
        return converter.convert(session);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Creates new session for bar",
            notes = "Provide categoryId of bar to add a new session with information from the request body to the bar",
            response = SessionResponse.class
    )
    public UUID createNewSession(
            @ApiParam(value = "ID value for the bar you want to create the session for") @PathVariable("barId") UUID barId,
            @Valid @RequestBody SessionRequest sessionRequest
    ) throws EntityNotFoundException {
        CreateSession command = new CreateSession(barId, sessionRequest.name);
        return sessionCommandHandler.handle(command);
    }

    @PutMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Creates new session for bar",
            notes = "Provide categoryId of bar to update the session with information from the request body",
            response = SessionResponse.class
    )
    public UUID updateSession(
            @ApiParam(value = "ID value for the bar you want to update the session from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to update") @PathVariable("sessionId") UUID sessionId,
            @Valid @RequestBody SessionRequest sessionRequest
    ) throws EntityNotFoundException {
        UpdateSession command = new UpdateSession(barId, sessionId, sessionRequest.name);
        return sessionCommandHandler.handle(command);
    }

    @PatchMapping("/{sessionId}/end")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @ApiOperation(
            value = "Ends the session of bar",
            notes = "Provide categoryId of bar and session to end the session of the bar",
            response = SessionResponse.class
    )
    public void endSession(
            @ApiParam(value = "ID value for the bar you want to end the session from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to end") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        EndSession command = new EndSession(barId, sessionId);
        sessionCommandHandler.handle(command);
    }

    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Deletes the session of bar",
            notes = "Provide categoryId of bar and session to delete the session from the bar",
            response = ProductResponse.class
    )
    public void deleteSession(
            @ApiParam(value = "ID value for the bar you want to delete the session from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to delete") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        DeleteSession command = new DeleteSession(sessionId);
        sessionCommandHandler.handle(command);
    }
}
