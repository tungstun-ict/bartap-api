package com.tungstun.barapi.domain.person;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {
    Person save(Person person);

    Person update(Person person);

    void delete(UUID id);

    Optional<Person> findById(UUID id);

//    List<Person> findAllByBarId(UUID barId);
}
