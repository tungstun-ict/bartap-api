package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringSessionRepository;
import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.exceptions.AlreadyActiveSessionException;
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

    /**
     * Returns a list with all sessions of bar
     * @return list of sessions
     * @throws NotFoundException if no bar with given id is found or
     *     if bar does not have any sessions
     */
    public List<Session> getAllSessionsOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        List<Session> sessions = bar.getSessions();
        if (sessions.isEmpty()) throw new NotFoundException("There are no sessions available for this bar");
        return sessions;
    }

    /**
     * Returns session with given id from bar with given id
     * @return session
     * @throws NotFoundException if no bar with given id is found or
     *     if bar does not have any sessions or
     *     if bar does not have a session with given id
     */
    public Session getSessionOfBar(Long barId, Long sessionId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        List<Session> sessions = bar.getSessions();
        if (sessions.isEmpty()) throw new NotFoundException("There are no sessions available for this bar");
        for(Session session : sessions){
            if(session.getId().equals(sessionId)) return session;
        }
        throw new NotFoundException("Bar does not have a session with id: " + sessionId);
    }

    /**
     * Creates a new session and adds it to bar with given id
     * @return created session
     * @throws NotFoundException if no bar with given id is found
     * @throws DuplicateRequestException if bar already has a duplicate of the session
     */
    public Session createNewSession(Long barId ) throws NotFoundException {
        Bar bar = BAR_SERVICE.getBar(barId);
        if(barHasActiveSession(bar)) throw new AlreadyActiveSessionException("Bar already has an active session.");
        Session session = Session.create();
        if(!bar.addSession(session)) throw new DuplicateRequestException("Bar already has this session");

        session = this.SPRING_SESSION_REPOSITORY.save(session);
        this.BAR_SERVICE.saveBar(bar);
        return session;
    }

    public Session endSession(Long barId, Long sessionId) throws NotFoundException {
        Session session = getSessionOfBar(barId, sessionId);
        session.setClosedDate(LocalDateTime.now());
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    private boolean barHasActiveSession(Bar bar) {
        for (Session session : bar.getSessions()) {
            if (session.getClosedDate() == null) return true;
        }
        return false;
    }

    public Session addBartenderToSession(Long barId, Long sessionId, Long bartenderId) throws NotFoundException {
        Session session = getSessionOfBar(barId, sessionId);
        Bartender bartender = this.PERSON_SERVICE.getBartenderOfBar(barId, bartenderId);
        if (sessionHasBartender(session, bartender))
            throw new NotFoundException(String.format("Session does not have a bartender with the id %s", bartender.getId()));
        bartender.addShift(session);
            session.addBartender(bartender);
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    private boolean sessionHasBartender(Session session, Bartender bartender) {
        for (Bartender bartenderIteration : session.getBartenders()) {
            System.out.println(bartenderIteration + " " + bartender);
            if  (bartenderIteration.equals(bartender)) return true;
        }
        return false;
    }

    public Session removeBartenderFromSession(Long barId, Long sessionId, Long bartenderId) throws NotFoundException {
        Session session = getSessionOfBar(barId, sessionId);
        Bartender bartender = findBartenderInSession(session, bartenderId);
        bartender.removeShift(session);
        session.removeBartender(bartender);
        return this.SPRING_SESSION_REPOSITORY.save(session);
    }

    private Bartender findBartenderInSession(Session session, Long bartenderId) throws NotFoundException {
        for (Bartender bartender : session.getBartenders()) {
            if (bartender.getId().equals(bartenderId)) return bartender;
        }
        throw new NotFoundException(String.format("Session does not have a bartender with the id %s", bartenderId));
    }

    /**
     * Removes session from sessions of bar with given id and deletes it from the datastore
     * @throws NotFoundException if no bar with given id is found 
     */
    public void deleteSession(Long barId, Long sessionId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Session session = getSessionOfBar(barId, sessionId);
        bar.removeSession(session);
        this.BAR_SERVICE.saveBar(bar);
        this.SPRING_SESSION_REPOSITORY.delete(session);
    }

    /**
     * Saves a Session object
     * @param session to be saved
     */
    public void saveSession(Session session){
        this.SPRING_SESSION_REPOSITORY.save(session);
    }


}
