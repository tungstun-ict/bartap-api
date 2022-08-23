package com.tungstun.barapi.application.session;

import com.tungstun.barapi.application.session.query.GetActiveSession;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.application.session.query.ListSessionsOfBar;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class SessionQueryHandler {
    private final SessionRepository sessionRepository;

    public SessionQueryHandler(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session handle(GetSession query) {
        return sessionRepository.findByIdAndBarId(query.sessionId(), query.barId())
                .orElseThrow(() -> new EntityNotFoundException("No Session found with id " + query.sessionId()));
    }
    public List<Session> handle(ListSessionsOfBar query) {
        return sessionRepository.findAllByBarId(query.barId());
    }

    public Session handle(GetActiveSession query) {
        return sessionRepository.findAllByBarId(query.barId())
                .stream()
                .filter(Session::isActive)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No active session found"));
    }
}
