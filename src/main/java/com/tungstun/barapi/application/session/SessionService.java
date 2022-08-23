package com.tungstun.barapi.application.session;

import com.tungstun.barapi.application.session.query.GetActiveSession;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionRepository;
import com.tungstun.barapi.exceptions.DuplicateActiveSessionException;
import com.tungstun.barapi.presentation.dto.request.SessionRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
public class SessionService {
    private final SessionQueryHandler sessionQueryHandler;
    private final SessionRepository sessionRepository;

    public SessionService(SessionQueryHandler sessionQueryHandler, SessionRepository sessionRepository) {
        this.sessionQueryHandler = sessionQueryHandler;
        this.sessionRepository = sessionRepository;
    }

    public Long createNewSession(Long barId, SessionRequest sessionRequest) throws EntityNotFoundException {
        try {
            sessionQueryHandler.handle(new GetActiveSession(barId));
            throw new DuplicateActiveSessionException("A session is already active, cannot create new session when an active session exists");
        } catch (EntityNotFoundException ignored) {
        }

        Session session = Session.create(barId, sessionRequest.name);
        return sessionRepository.save(session).getId();
    }

    public Long updateSession(Long barId, Long sessionId, SessionRequest sessionRequest) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        session.setName(sessionRequest.name);
        return sessionRepository.save(session).getId();
    }

    public void endSession(Long barId, Long sessionId) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        session.end();
        sessionRepository.save(session);
    }

    public void lockSession(Long barId, Long sessionId) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        session.lock();
        sessionRepository.save(session);
    }

    public void deleteSession(Long sessionId) throws EntityNotFoundException {
        sessionRepository.delete(sessionId);
    }
}
