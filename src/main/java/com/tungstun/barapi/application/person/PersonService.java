package com.tungstun.barapi.application.person;

import com.tungstun.barapi.application.bar.BarCommandHandler;
import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonRepository;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonQueryHandler personQueryHandler;
    private final BarRepository barRepository;
    private final BarQueryHandler barQueryHandler;
    private final BarCommandHandler barCommandHandler;

    public PersonService(PersonRepository personRepository, PersonQueryHandler personQueryHandler, BarRepository barRepository, BarQueryHandler barQueryHandler, BarCommandHandler barCommandHandler) {
        this.personRepository = personRepository;
        this.personQueryHandler = personQueryHandler;
        this.barRepository = barRepository;
        this.barQueryHandler = barQueryHandler;
        this.barCommandHandler = barCommandHandler;
    }

//    public Person getPersonOfBar(Long barId, Long personId) throws EntityNotFoundException {
//        List<Person> people = getAllPeopleOfBar(barId);
//        return people.stream()
//                .filter(person -> person.getId().equals(personId))
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("Bar does not have a person with categoryId: " + personId));
//    }
//    public Person getPersonOfBar(Long barId, String username) throws EntityNotFoundException {
//        List<Person> people = getAllPeopleOfBar(barId);
//        System.out.println(people.get(0).getUser().getUsername());
//        return people.stream()
//                .filter(person -> person.getUser() != null)
//                .filter(person -> person.getUser().getUsername().equals(username))
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("Bar does not have a person with username: " + username));
//    }
//
//    public List<Person> getAllPeopleOfBar(Long barId) throws EntityNotFoundException {
//        return personRepository.findAllByBarId(barId);
////        Bar bar = this.barService.getBar(barId);
////        return bar.getPeople();
//    }

    public UUID createNewPerson(UUID barId, PersonRequest personRequest) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(barId));
        Person person = bar.createPerson(personRequest.name);
        barRepository.save(bar);
        return person.getId();
    }

    public UUID updatePerson(UUID barId, UUID personId, PersonRequest personRequest) throws EntityNotFoundException {
        Person person = personQueryHandler.handle(new GetPerson(personId, barId));
        person.setName(personRequest.name);
        personRepository.save(person);
        return person.getId();
    }

    public void deletePersonFromBar(UUID barId, UUID personId) throws EntityNotFoundException {
        personRepository.delete(personId);
//        Bar bar = this.barService.getBar(barId);
//        Person person = getPersonOfBar(barId, personId);
//        bar.removePerson(person);
//        this.barService.saveBar(bar);
    }
}
