package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringSessionRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.exceptions.DuplicateActiveSessionException;
import com.tungstun.barapi.exceptions.InvalidSessionStateException;
import com.tungstun.barapi.presentation.dto.request.SessionRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class SessionService {
    private final SpringSessionRepository SPRING_SESSION_REPOSITORY;
    private final BarService BAR_SERVICE;

    public SessionService(SpringSessionRepository springSessionRepository, BarService barService) {
        this.SPRING_SESSION_REPOSITORY = springSessionRepository;
        this.BAR_SERVICE = barService;
    }

    public List<Session> getAllSessionsOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        return bar.getSessions();
    }

    public Session getSessionOfBar(Long barId, Long sessionId) throws NotFoundException {
        List<Session> sessions = getAllSessionsOfBar(barId);
        return sessions.stream()
                .filter(session -> session.getId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Bar does not have a session with id: " + sessionId));
    }

    public Session getActiveSessionOfBar(Long barId) throws NotFoundException {
        List<Session> sessions = getAllSessionsOfBar(barId);
        return findActiveSession(sessions);
    }

    private Session findActiveSession(List<Session> sessions) throws NotFoundException {
        return sessions.stream()
                .filter(Session::isActive)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No active session found"));
    }

    public Session createNewSession(Long barId, SessionRequest sessionRequest) throws NotFoundException {
        Bar bar = BAR_SERVICE.getBar(barId);
        if(barHasActiveSession(bar)) throw new DuplicateActiveSessionException("Bar already has an active session.");
        Session session = Session.create(sessionRequest.name);
        bar.addSession(session);
        session = this.SPRING_SESSION_REPOSITORY.save(session);
        this.BAR_SERVICE.saveBar(bar);
        return session;
    }

    private boolean barHasActiveSession(Bar bar) {
        return bar.getSessions()
                .stream()
                .anyMatch(Session::isActive);
    }

    public Session updateSession(Long barId, Long sessionId, SessionRequest sessionRequest) throws NotFoundException {
        Session session = getSessionIfEditable(barId, sessionId);
        session.setName(sessionRequest.name);
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    public Session endSession(Long barId, Long sessionId) throws NotFoundException {
        Session session = getSessionIfEditable(barId, sessionId);
        session.endSession();
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    private Session getSessionIfEditable(Long barId, Long sessionId) throws NotFoundException {
        Session session = getSessionOfBar(barId, sessionId);
        checkEditable(session);
        return session;
    }

    public Session lockSession(Long barId, Long sessionId) throws NotFoundException {
        Session session = getSessionOfBar(barId, sessionId);
        if (session.isLocked()) throw new InvalidSessionStateException("Cannot lock an already locked session");
        session.lock();
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    public void checkEditable(Session session) {
        if (session.getClosedDate() != null || session.isLocked())
            throw new InvalidSessionStateException("Cannot make changes to session if session is not active");
    }

    public void deleteSession(Long barId, Long sessionId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Session session = getSessionOfBar(barId, sessionId);
        bar.removeSession(session);
        this.BAR_SERVICE.saveBar(bar);
        this.SPRING_SESSION_REPOSITORY.delete(session);
    }

    public void saveSession(Session session){
        this.SPRING_SESSION_REPOSITORY.save(session);
    }
}
