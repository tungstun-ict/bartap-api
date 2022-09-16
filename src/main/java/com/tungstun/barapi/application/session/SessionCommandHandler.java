package com.tungstun.barapi.application.session;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.session.command.CreateSession;
import com.tungstun.barapi.application.session.command.DeleteSession;
import com.tungstun.barapi.application.session.command.EndSession;
import com.tungstun.barapi.application.session.command.UpdateSession;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class SessionCommandHandler {
    private final BarQueryHandler barQueryHandler;
    private final SessionQueryHandler sessionQueryHandler;
    private final BarRepository barRepository;
    private final SessionRepository sessionRepository;

    public SessionCommandHandler(BarQueryHandler barQueryHandler, SessionQueryHandler sessionQueryHandler, BarRepository barRepository, SessionRepository sessionRepository) {
        this.barQueryHandler = barQueryHandler;
        this.sessionQueryHandler = sessionQueryHandler;
        this.barRepository = barRepository;
        this.sessionRepository = sessionRepository;
    }

    public UUID handle(CreateSession command) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(command.barId()));
        Session session = bar.newSession(command.name());
        barRepository.save(bar);
        return session.getId();
    }

    public UUID handle(UpdateSession command) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(command.barId(), command.sessionId()));
        session.setName(command.name());
        return sessionRepository.save(session).getId();
    }

    public void handle(DeleteSession command) throws EntityNotFoundException {
        sessionRepository.delete(command.sessionId());
    }

    public void handle(EndSession command) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(command.barId(), command.sessionId()));
        session.end();
        sessionRepository.save(session);
    }
}
