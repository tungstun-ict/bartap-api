package com.tungstun.barapi.application.person;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.person.command.CreatePerson;
import com.tungstun.barapi.application.person.command.CreatePersonConnectionToken;
import com.tungstun.barapi.application.person.command.DeletePerson;
import com.tungstun.barapi.application.person.command.UpdatePerson;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonRepository;
import com.tungstun.security.domain.jwt.JwtTokenGenerator;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@Service
public class PersonCommandHandler {
    private final PersonRepository personRepository;
    private final PersonQueryHandler personQueryHandler;
    private final BarRepository barRepository;
    private final BarQueryHandler barQueryHandler;
    private final JwtTokenGenerator jwtTokenGenerator;

    public PersonCommandHandler(PersonRepository personRepository, PersonQueryHandler personQueryHandler, BarRepository barRepository, BarQueryHandler barQueryHandler, JwtTokenGenerator jwtTokenGenerator) {
        this.personRepository = personRepository;
        this.personQueryHandler = personQueryHandler;
        this.barRepository = barRepository;
        this.barQueryHandler = barQueryHandler;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public UUID handle(CreatePerson command) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(command.barId()));
        Person person = bar.createPerson(command.name());
        barRepository.save(bar);
        return person.getId();
    }

    public UUID handle(UpdatePerson command) throws EntityNotFoundException {
        Person person = personQueryHandler.handle(new GetPerson(command.barId(), command.personId()));
        person.setName(command.name());
        personRepository.save(person);
        return person.getId();
    }

    public void handle(DeletePerson command) throws EntityNotFoundException {
        personRepository.delete(command.personId());
    }

    public String handle(CreatePersonConnectionToken query) {
        Person person = personQueryHandler.handle(new GetPerson(query.barId(), query.personId()));
        if (person.getUser() != null) {
            throw new IllegalStateException(String.format("Person with id %s is already connected to a user", query.personId()));
        }
        return jwtTokenGenerator.createPersonConnectionToken(query.barId(), query.personId());
    }
}
