package com.tungstun.barapi.application.person;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.person.command.*;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonRepository;
import com.tungstun.security.application.user.UserQueryHandler;
import com.tungstun.security.domain.jwt.JwtTokenGenerator;
import com.tungstun.security.domain.jwt.JwtValidator;
import com.tungstun.security.domain.user.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@Service
public class PersonCommandHandler {
    private final PersonRepository personRepository;
    private final PersonQueryHandler personQueryHandler;
    private final UserQueryHandler userQueryHandler;
    private final BarRepository barRepository;
    private final BarQueryHandler barQueryHandler;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtValidator jwtValidator;

    public PersonCommandHandler(PersonRepository personRepository, PersonQueryHandler personQueryHandler, UserQueryHandler userQueryHandler, BarRepository barRepository, BarQueryHandler barQueryHandler, JwtTokenGenerator jwtTokenGenerator, JwtValidator jwtValidator) {
        this.personRepository = personRepository;
        this.personQueryHandler = personQueryHandler;
        this.userQueryHandler = userQueryHandler;
        this.barRepository = barRepository;
        this.barQueryHandler = barQueryHandler;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtValidator = jwtValidator;
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

    public String handle(CreatePersonConnectionToken command) {
        Person person = personQueryHandler.handle(new GetPerson(command.barId(), command.personId()));
        if (person.getUser() != null) {
            throw new IllegalStateException(String.format("Person with id %s is already connected to a user", command.personId()));
        }
        return jwtTokenGenerator.createPersonConnectionToken(command.barId(), command.personId());
    }

    public void handle(ConnectUserToPerson command) {
        DecodedJWT jwt = jwtValidator.verifyToken(command.token());
        UUID barId = UUID.fromString(jwt.getClaim("bar_id").asString());
        UUID personId = UUID.fromString(jwt.getClaim("person_id").asString());

        User user = (User) userQueryHandler.loadUserByUsername(command.username());
        Bar bar = barQueryHandler.handle(new GetBar(barId));
        bar.connectUserToPerson(user, personId);
        barRepository.save(bar);
    }
}
