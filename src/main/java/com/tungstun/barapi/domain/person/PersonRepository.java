package com.tungstun.barapi.domain.person;

import java.util.Optional;

public interface PersonRepository {
    Person save(Person person);

    Person update(Person person);

    void delete(Long id);

    Optional<Person> findById(Long id);
}
