package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringSessionRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.exceptions.InvalidSessionStateException;
import com.tungstun.barapi.presentation.dto.request.SessionRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class SessionService {
    private final SpringSessionRepository springSessionRepository;
    private final BarService barService;

    public SessionService(SpringSessionRepository springSessionRepository, BarService barService) {
        this.springSessionRepository = springSessionRepository;
        this.barService = barService;
    }

    public List<Session> getAllSessionsOfBar(Long barId) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        return bar.getSessions();
    }

    public Session getSessionOfBar(Long barId, Long sessionId) throws EntityNotFoundException {
        List<Session> sessions = getAllSessionsOfBar(barId);
        return sessions.stream()
                .filter(session -> session.getId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Bar does not have a session with id: " + sessionId));
    }

    public Session getActiveSessionOfBar(Long barId) throws EntityNotFoundException {
        Session session = this.barService.getBar(barId).activeSession();
        if (session == null) throw new EntityNotFoundException("No active session found");
        return session;
    }

    public Session createNewSession(Long barId, SessionRequest sessionRequest) throws EntityNotFoundException {
        Bar bar = barService.getBar(barId);
        bar.newSession(sessionRequest.name);
        bar = this.barService.saveBar(bar);
        return bar.activeSession();
    }

    public Session updateSession(Long barId, Long sessionId, SessionRequest sessionRequest) throws EntityNotFoundException {
        Session session = getSessionIfEditable(barId, sessionId);
        session.setName(sessionRequest.name);
        return this.springSessionRepository.save(session);
    }

    public Session endSession(Long barId, Long sessionId) throws EntityNotFoundException {
        Session session = getSessionIfEditable(barId, sessionId);
        session.endSession();
        return this.springSessionRepository.save(session);
    }

    private Session getSessionIfEditable(Long barId, Long sessionId) throws EntityNotFoundException {
        Session session = getSessionOfBar(barId, sessionId);
        checkEditable(session);
        return session;
    }

    public void checkEditable(Session session) {
        if (session.getClosedDate() != null || session.isLocked())
            throw new InvalidSessionStateException("Cannot make changes to session if session is not active");
    }

    public Session lockSession(Long barId, Long sessionId) throws EntityNotFoundException {
        Session session = getSessionOfBar(barId, sessionId);
        if (session.isLocked()) throw new InvalidSessionStateException("Cannot lock an already locked session");
        session.lock();
        return this.springSessionRepository.save(session);
    }

    public void deleteSession(Long barId, Long sessionId) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        Session session = getSessionOfBar(barId, sessionId);
        bar.removeSession(session);
        this.barService.saveBar(bar);
        this.springSessionRepository.delete(session);
    }

    public void saveSession(Session session){
        this.springSessionRepository.save(session);
    }
}
