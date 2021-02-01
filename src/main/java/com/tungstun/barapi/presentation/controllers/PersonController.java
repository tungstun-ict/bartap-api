package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.PersonService;
import com.tungstun.barapi.domain.Bar;
import com.tungstun.barapi.domain.Bill;
import com.tungstun.barapi.domain.Person;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import com.tungstun.barapi.presentation.dto.response.PersonResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("bars/{barId}/users")
public class PersonController {
    private final ResponseMapper RESPONSE_MAPPER;
    private final PersonService PERSON_SERVICE;

    public PersonController(PersonService personService, ResponseMapper responseMapper) {
        this.PERSON_SERVICE = personService;
        this.RESPONSE_MAPPER = responseMapper;
    }

    /**
     * Converts Person object to a PersonResponse object ready for request response
     * @param person person to be converted
     * @return PersonResponse
     */
    private PersonResponse convertToPersonResponse(Person person){
        return this.RESPONSE_MAPPER.convert(person, PersonResponse.class);
    }

    /**
     * Converts List of person objects to a List of PersonResponse objects ready for request response
     * @param people people to be converted
     * @return List<PersonResponse>
     */
    private List<PersonResponse> convertToPersonResponsesList(List<Person> people){
        return this.RESPONSE_MAPPER.convertList(people, PersonResponse.class);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPeopleOfBar(
            @PathVariable Long barId)
            throws NotFoundException {
        List<Person> allPeople = this.PERSON_SERVICE.getAllPeopleOfBar(barId);
        List<PersonResponse> peopleResponses = convertToPersonResponsesList(allPeople);
        return new ResponseEntity<>(peopleResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/{personId}")
    public ResponseEntity<PersonResponse> getPersonOfBar(
            @PathVariable("barId") Long barId,
            @PathVariable("personId") Long personId)
            throws NotFoundException {
        Person person = this.PERSON_SERVICE.getPersonOfBar(barId, personId);
        return new ResponseEntity<>(convertToPersonResponse(person),  HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PersonResponse> createNewPersonForBar(
            @PathVariable("barId") Long barId,
            @Valid @RequestBody PersonRequest personRequest)
            throws NotFoundException {
        Person person = this.PERSON_SERVICE.createNewPerson(barId, personRequest);
        return new ResponseEntity<>(convertToPersonResponse(person),  HttpStatus.CREATED);
    }

    @PatchMapping("/{personId}")
    public ResponseEntity<PersonResponse> updatePerson(
            @PathVariable("barId") Long barId,
            @PathVariable("personId") Long personId,
            @RequestBody PersonRequest personRequest)
            throws NotFoundException {
        Person person = this.PERSON_SERVICE.updatePerson(barId, personId, personRequest);
        return new ResponseEntity<>(convertToPersonResponse(person), HttpStatus.OK);
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> deletePerson(
            @PathVariable("barId") Long barId,
            @PathVariable("personId") Long personId)
            throws NotFoundException {
        this.PERSON_SERVICE.deletePersonFromBar(barId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
