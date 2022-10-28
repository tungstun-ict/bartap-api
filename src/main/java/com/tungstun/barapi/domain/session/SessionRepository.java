package com.tungstun.barapi.domain.session;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {
    Session save(Session session);

    Session update(Session session);

    void delete(UUID id);

    Optional<Session> findById(UUID id);
}
