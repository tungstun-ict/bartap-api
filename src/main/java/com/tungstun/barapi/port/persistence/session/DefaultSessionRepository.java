package com.tungstun.barapi.port.persistence.session;

import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultSessionRepository implements SessionRepository {
    private final SpringSessionRepository repository;

    public DefaultSessionRepository(SpringSessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Session save(Session session) {
        return repository.save(session);
    }

    @Override
    public Session update(Session session) {
        return repository.save(session);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return repository.findById(id);
    }
}
