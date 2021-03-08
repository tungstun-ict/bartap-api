package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringPersonRepository;
import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.Customer;
import com.tungstun.barapi.domain.Person;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    private SpringPersonRepository SPRING_PERSON_REPOSITORY;
    private BarService BAR_SERVICE;

    public PersonService(SpringPersonRepository SPRING_PERSON_REPOSITORY, BarService BAR_SERVICE) {
        this.SPRING_PERSON_REPOSITORY = SPRING_PERSON_REPOSITORY;
        this.BAR_SERVICE = BAR_SERVICE;
    }

    /**
     * Returns a list with all people related to a bar
     * @return list of people
     * @throws NotFoundException if no bar with given id is found or
     *      if bar does not have any people
     */
    public List<Person> getAllPeopleOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        List<Person> people = bar.getUsers();
        if (people.isEmpty()) throw new NotFoundException("There are no people available for this bar");
        return people;
    }

    /**
     * Returns person with given id from bar with given id
     * @return person
     * @throws NotFoundException if no bar with given id is found or
     *     if bar does not have any people or
     *     if bar does not have a person with given id
     */
    public Person getPersonOfBar(Long barId, Long personId) throws NotFoundException {
        List<Person> people = getAllPeopleOfBar(barId);
        for(Person person : people){
            if(person.getId().equals(personId)) return person;
        }
        throw new NotFoundException("Bar does not have a person with id: " + personId);
    }

    /**
     * Creates a new person and adds it to the bar with given id
     * @return created person
     * @throws NotFoundException if no bar with given id is found or
     */
    public Person createNewPerson(Long barId, PersonRequest personRequest) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        for (Person person : bar.getUsers()) {
            if (person.getName().equals(personRequest.name)) {
                throw new DuplicateRequestException(
                        String.format("User with name '%s' already exists", personRequest.name));
            }
        }
        Person person;
        if(personRequest.phoneNumber != null) {
            person = new Customer(personRequest.name, personRequest.phoneNumber, new ArrayList<>());
        }else {
            person = new Bartender(personRequest.name);
        }
        bar.addUser(person);
        this.BAR_SERVICE.saveBar(bar);
        return person;
    }

    /**
     * Edits person object with all values that are not null.
     * If person is of type customer and phonenumber is present, updates phonenumber
     * Returns altered person, if succesfull.
     * @return edited person
     * @throws NotFoundException if no bar with given id is found or
     *      *      if bar does not have any people or
     *      *      if bar does not have a person with given id
     */
    public Person updatePerson(Long barId, Long personId, PersonRequest personRequest) throws NotFoundException {
        Person person = getPersonOfBar(barId, personId);
        if (personRequest.phoneNumber != null &&
                person instanceof Customer) {
            ((Customer) person).setPhoneNumber(personRequest.phoneNumber);
        }
        if (personRequest.name != null){
            person.setName(personRequest.name);
        }
        return this.SPRING_PERSON_REPOSITORY.save(person);
    }

    /**
     * Deletes person with given id from bar with given id
     * @throws NotFoundException if no bar with given id is found or
     *      if bar does not have any people or
     *      if bar does not have a person with given id
     */
    public void deletePersonFromBar(Long barId, Long personId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Person person = getPersonOfBar(barId, personId);
        bar.removeUser(person);
        this.BAR_SERVICE.saveBar(bar);
    }

    /**
     * Finds a person with id then checks if it is a Bartender by casting the person to it
     * @throws NotFoundException if no bartender with given id was found
     */
    public Bartender getBartenderOfBar(Long barId, Long bartenderId) throws NotFoundException {
        Person person = getPersonOfBar(barId, bartenderId);
        if (!(person instanceof Bartender)) throw new NotFoundException(String.format("No Bartender found with id %s", bartenderId));
        return (Bartender) person;
    }

    /**
     * Finds a person with id then checks if it is a Customer by casting the person to it
     * @throws NotFoundException if no customer with given id was found
     */
    public Customer getCustomerOfBar(Long barId, Long customerId) throws NotFoundException {
        Person person = getPersonOfBar(barId, customerId);
        if (!(person instanceof Customer)) throw new NotFoundException(String.format("No Customer found with id %s", customerId));
        return (Customer) person;
    }
}
