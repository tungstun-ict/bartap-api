package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface SpringSessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findById(Long id);
}
