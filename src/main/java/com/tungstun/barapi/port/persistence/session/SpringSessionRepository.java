package com.tungstun.barapi.port.persistence.session;

import com.tungstun.barapi.domain.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringSessionRepository extends JpaRepository<Session, UUID> {
}
