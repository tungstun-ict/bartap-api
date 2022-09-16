package com.tungstun.security.domain.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository {
    User save(User user);

    User update(User user);

    void delete(UUID id);

    void delete(User id);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByMail(String mail);
}
