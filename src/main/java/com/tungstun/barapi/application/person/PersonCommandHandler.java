package com.tungstun.barapi.application.person;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.person.command.CreatePerson;
import com.tungstun.barapi.application.person.command.UpdatePerson;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonRepository;
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

    public PersonCommandHandler(PersonRepository personRepository, PersonQueryHandler personQueryHandler, BarRepository barRepository, BarQueryHandler barQueryHandler) {
        this.personRepository = personRepository;
        this.personQueryHandler = personQueryHandler;
        this.barRepository = barRepository;
        this.barQueryHandler = barQueryHandler;
    }

    public UUID createNewPerson(CreatePerson command) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(command.barId()));
        Person person = bar.createPerson(command.name());
        barRepository.save(bar);
        return person.getId();
    }

    public UUID updatePerson(UpdatePerson command) throws EntityNotFoundException {
        Person person = personQueryHandler.handle(new GetPerson(command.personId(), command.barId()));
        person.setName(command.name());
        personRepository.save(person);
        return person.getId();
    }

    public void deletePersonFromBar(UUID barId, UUID personId) throws EntityNotFoundException {
        personRepository.delete(personId);
    }
}
