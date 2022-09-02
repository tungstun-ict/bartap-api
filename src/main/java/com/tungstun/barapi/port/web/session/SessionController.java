package com.tungstun.barapi.port.web.session;

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
import com.tungstun.barapi.port.web.session.converter.SessionConverter;
import com.tungstun.barapi.port.web.session.request.CreateSessionRequest;
import com.tungstun.barapi.port.web.session.request.UpdateSessionRequest;
import com.tungstun.barapi.port.web.session.response.SessionResponse;
import com.tungstun.common.response.UuidResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(
            summary = "Finds sessions of a bar",
            description = "Find all session of a bar with the given id's"
    )
    public List<SessionResponse> getAllBarSessions(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId
    ) throws EntityNotFoundException {
        List<Session> allSessions = sessionQueryHandler.handle(new ListSessionsOfBar(barId));
        return converter.convertAll(allSessions);
    }

    @GetMapping(path = "/active")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds active session of a bar",
            description = "Find the currently active session of a bar with the given id if one is active"
    )
    public SessionResponse getActiveBarSessions(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId
    ) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetActiveSession(barId));
        return converter.convert(session);
    }

    @GetMapping(path = "/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds session of a bar",
            description = "Find a session of a bar with the given id's"
    )
    public SessionResponse getBarSessionsById(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(barId, sessionId));
        return converter.convert(session);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Creates a new session for a bar",
            description = "Create a new session for a bar with the given id if there is no currently active bar"
    )
    public UuidResponse createNewSession(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Valid @RequestBody CreateSessionRequest request
    ) throws EntityNotFoundException {
        CreateSession command = new CreateSession(barId, request.name());
        return new UuidResponse(sessionCommandHandler.handle(command));
    }

    @PutMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Updates a session for a bar",
            description = "Update a session of a bar with the given information"
    )
    public UuidResponse updateSession(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId,
            @Valid @RequestBody UpdateSessionRequest request
    ) throws EntityNotFoundException {
        UpdateSession command = new UpdateSession(barId, sessionId, request.name());
        return new UuidResponse(sessionCommandHandler.handle(command));
    }

    @PatchMapping("/{sessionId}/end")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @Operation(
            summary = "Ends a session of a bar",
            description = "End a session of a bar with the given id's"
    )
    public void endSession(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        EndSession command = new EndSession(barId, sessionId);
        sessionCommandHandler.handle(command);
    }

    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Deletes a session of a bar",
            description = "Delete a session of a bar with the given id's"
    )
    public void deleteSession(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        DeleteSession command = new DeleteSession(sessionId);
        sessionCommandHandler.handle(command);
    }
}
