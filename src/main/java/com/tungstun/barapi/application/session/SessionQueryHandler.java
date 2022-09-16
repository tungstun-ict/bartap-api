package com.tungstun.barapi.application.session;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.session.query.GetActiveSession;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.application.session.query.ListSessionsOfBar;
import com.tungstun.barapi.domain.session.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class SessionQueryHandler {
    private final BarQueryHandler barQueryHandler;

    public SessionQueryHandler(BarQueryHandler barQueryHandler) {
        this.barQueryHandler = barQueryHandler;
    }

    public Session handle(GetSession query) {
        return barQueryHandler.handle(new GetBar(query.barId()))
                .getSessions()
                .stream()
                .filter(session -> session.getId().equals(query.sessionId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No Session found with id " + query.sessionId()));

    }

    public List<Session> handle(ListSessionsOfBar query) {
        return barQueryHandler.handle(new GetBar(query.barId()))
                .getSessions();
    }

    public Session handle(GetActiveSession query) {
        return barQueryHandler.handle(new GetBar(query.barId()))
                .getActiveSession();
    }
}
