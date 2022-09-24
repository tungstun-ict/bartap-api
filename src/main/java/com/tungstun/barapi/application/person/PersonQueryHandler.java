package com.tungstun.barapi.application.person;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.application.person.query.GetPersonByUserUsername;
import com.tungstun.barapi.application.person.query.ListPeopleOfBar;
import com.tungstun.barapi.domain.person.Person;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PersonQueryHandler {
    private final BarQueryHandler barQueryHandler;

    public PersonQueryHandler(BarQueryHandler barQueryHandler) {
        this.barQueryHandler = barQueryHandler;
    }

    public Person handle(GetPerson query) {
        return barQueryHandler.handle(new GetBar(query.barId()))
                .getPeople()
                .stream()
                .filter(person  -> person.getId().equals(query.personId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No Person found with id: " + query.personId()));
    }

    public Person handle(GetPersonByUserUsername query) {
        return barQueryHandler.handle(new GetBar(query.barId()))
                .getPeople()
                .stream()
                .filter(person -> person.getUser() != null)
                .filter(person -> person.getUser().getId().equals(query.userId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Bar does not have a person with user with id: " + query.userId()));
    }

    public List<Person> handle(ListPeopleOfBar query) {
        return barQueryHandler.handle(new GetBar(query.barId()))
                .getPeople();
    }
}
