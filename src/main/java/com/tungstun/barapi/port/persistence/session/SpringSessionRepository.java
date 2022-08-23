package com.tungstun.barapi.port.persistence.session;

import com.tungstun.barapi.domain.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringSessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByIdAndBarId(Long id, Long barId);

    List<Session> findAllByBarId(Long id);
}
