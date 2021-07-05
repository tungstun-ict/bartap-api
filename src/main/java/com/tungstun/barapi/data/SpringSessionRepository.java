package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringSessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findById(Long id);
}
