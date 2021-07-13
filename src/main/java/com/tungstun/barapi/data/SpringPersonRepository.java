package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface SpringPersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findById(Long id);
}
