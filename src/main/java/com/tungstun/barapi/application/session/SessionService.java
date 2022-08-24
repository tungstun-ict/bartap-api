package com.tungstun.barapi.application.session;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionRepository;
import com.tungstun.barapi.presentation.dto.request.SessionRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class SessionService {
    private final BarQueryHandler barQueryHandler;
    private final SessionQueryHandler sessionQueryHandler;
    private final BarRepository barRepository;
    private final SessionRepository sessionRepository;

    public SessionService(BarQueryHandler barQueryHandler, SessionQueryHandler sessionQueryHandler, BarRepository barRepository, SessionRepository sessionRepository) {
        this.barQueryHandler = barQueryHandler;
        this.sessionQueryHandler = sessionQueryHandler;
        this.barRepository = barRepository;
        this.sessionRepository = sessionRepository;
    }

    public UUID createNewSession(UUID barId, SessionRequest sessionRequest) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(barId));
        Session session = sessionRepository.save(bar.newSession(sessionRequest.name));
        barRepository.save(bar);
//        try {
//            sessionQueryHandler.handle(new GetActiveSession(barId));
//            throw new DuplicateActiveSessionException("A session is already active, cannot create new session when an active session exists");
//        } catch (EntityNotFoundException ignored) {
//        }
//        Session session = Session.create(barId, sessionRequest.name);
        return session.getId();
    }

    public UUID updateSession(UUID barId, UUID sessionId, SessionRequest sessionRequest) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        session.setName(sessionRequest.name);
        return sessionRepository.save(session).getId();
    }

    public void endSession(UUID barId, UUID sessionId) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        session.end();
        sessionRepository.save(session);
    }

    public void deleteSession(UUID sessionId) throws EntityNotFoundException {
        sessionRepository.delete(sessionId);
    }
}
