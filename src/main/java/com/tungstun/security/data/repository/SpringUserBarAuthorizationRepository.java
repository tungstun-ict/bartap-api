package com.tungstun.security.data.repository;

import com.tungstun.security.data.model.UserBarAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringUserBarAuthorizationRepository extends JpaRepository<UserBarAuthorization, Long> {
    Optional<UserBarAuthorization> findById(Long id);
}
