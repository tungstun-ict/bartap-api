package com.tungstun.barapi.domain.session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository {
    Session save(Session session);

    Session update(Session session);

    void delete(Long id);

    Optional<Session> findById(Long id);

    Optional<Session> findByIdAndBarId(Long id, Long barId);

    List<Session> findAllByBarId(Long id);
}
