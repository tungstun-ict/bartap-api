package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringSessionRepository;
import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.exceptions.DuplicateActiveSessionException;
import com.tungstun.barapi.exceptions.LockedSessionException;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {
    private final SpringSessionRepository SPRING_SESSION_REPOSITORY;
    private final BarService BAR_SERVICE;
    private final PersonService PERSON_SERVICE;

    public SessionService(SpringSessionRepository springSessionRepository, BarService barService, PersonService personService) {
        this.SPRING_SESSION_REPOSITORY = springSessionRepository;
        this.BAR_SERVICE = barService;
        this.PERSON_SERVICE = personService;
    }

    public List<Session> getAllSessionsOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        List<Session> sessions = bar.getSessions();
        if (sessions.isEmpty()) throw new NotFoundException("There are no sessions available for this bar");
        return sessions;
    }

    public Session getSessionOfBar(Long barId, Long sessionId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        List<Session> sessions = bar.getSessions();
        if (sessions.isEmpty()) throw new NotFoundException("There are no sessions available for this bar");
        for(Session session : sessions){
            if(session.getId().equals(sessionId)) return session;
        }
        throw new NotFoundException("Bar does not have a session with id: " + sessionId);
    }

    public Session createNewSession(Long barId ) throws NotFoundException {
        Bar bar = BAR_SERVICE.getBar(barId);
        if(barHasActiveSession(bar)) throw new DuplicateActiveSessionException("Bar already has an active session.");
        Session session = Session.create();
        if(!bar.addSession(session)) throw new DuplicateRequestException("Bar already has this session");
        session = this.SPRING_SESSION_REPOSITORY.save(session);
        this.BAR_SERVICE.saveBar(bar);
        return session;
    }
    private boolean barHasActiveSession(Bar bar) {
        for (Session session : bar.getSessions()) {
            if (session.getClosedDate() == null) return true;
        }
        return false;
    }

    public Session endSession(Long barId, Long sessionId) throws NotFoundException {
        Session session = getSessionIfActive(barId, sessionId);
        session.setClosedDate(LocalDateTime.now());
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    public Session lockSession(Long barId, Long sessionId) throws NotFoundException {
        Session session = getSessionIfActive(barId, sessionId);
        if (session.getClosedDate() != null) session.setClosedDate(LocalDateTime.now());
        session.lock();
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    private Session getSessionIfActive(Long barId, Long sessionId) throws NotFoundException {
        Session session = getSessionOfBar(barId, sessionId);
        sessionIsActive(session);
        return session;
    }

    public void sessionIsActive(Session session) {
        if (session.isLocked())
            throw new LockedSessionException("Cannot make changes to session if session has ended");
    }

    public Session addBartenderToSession(Long barId, Long sessionId, Long bartenderId) throws NotFoundException {
        Session session = getSessionIfActive(barId, sessionId);
        Bartender bartender = this.PERSON_SERVICE.getBartenderOfBar(barId, bartenderId);
        if (sessionHasBartender(session, bartender))
            throw new NotFoundException(String.format("Session does not have a bartender with the id %s", bartender.getId()));
        session.addBartender(bartender);
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    private boolean sessionHasBartender(Session session, Bartender bartender) {
        for (Bartender bartenderIteration : session.getBartenders()) {
            if  (bartenderIteration.equals(bartender)) return true;
        }
        return false;
    }

    public Session removeBartenderFromSession(Long barId, Long sessionId, Long bartenderId) throws NotFoundException {
        Session session = getSessionIfActive(barId, sessionId);
        Bartender bartender = findBartenderInSession(session, bartenderId);
        session.removeBartender(bartender);
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    private Bartender findBartenderInSession(Session session, Long bartenderId) throws NotFoundException {
        for (Bartender bartender : session.getBartenders()) {
            if (bartender.getId().equals(bartenderId)) return bartender;
        }
        throw new NotFoundException(String.format("Session does not have a bartender with the id %s", bartenderId));
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
