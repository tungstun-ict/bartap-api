package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.PersonService;
import com.tungstun.barapi.domain.Person;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import com.tungstun.barapi.presentation.dto.response.PersonResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bars/{barId}/people")
public class PersonController {
    private final ResponseMapper RESPONSE_MAPPER;
    private final PersonService PERSON_SERVICE;

    public PersonController(PersonService personService, ResponseMapper responseMapper) {
        this.PERSON_SERVICE = personService;
        this.RESPONSE_MAPPER = responseMapper;
    }

    private PersonResponse convertToPersonResponse(Person person){
        return this.RESPONSE_MAPPER.convert(person, PersonResponse.class);
    }

    private List<PersonResponse> convertToPersonResponsesList(List<Person> people){
        return this.RESPONSE_MAPPER.convertList(people, PersonResponse.class);
    }

    @GetMapping
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds all people of bar",
            notes = "Provide id of bar to look up all people that are linked to the bar",
            response = PersonResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<PersonResponse>> getAllPeopleOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve people from") @PathVariable Long barId
    ) throws NotFoundException {
        List<Person> allPeople = this.PERSON_SERVICE.getAllPeopleOfBar(barId);
        return new ResponseEntity<>(convertToPersonResponsesList(allPeople), HttpStatus.OK);
    }

    @GetMapping(path = "/{personId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds person of bar",
            notes = "Provide id of bar and person to look up a specific person of the bar",
            response = PersonResponse.class
    )
    public ResponseEntity<PersonResponse> getPersonOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the person from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the person you want to retrieve") @PathVariable("personId") Long personId
    ) throws NotFoundException {
        Person person = this.PERSON_SERVICE.getPersonOfBar(barId, personId);
        return new ResponseEntity<>(convertToPersonResponse(person),  HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Creates person for bar",
            notes = "Provide id of bar to create a new person with the information from the request body for the bar",
            response = PersonResponse.class
    )
    public ResponseEntity<PersonResponse> createNewPersonForBar(
            @ApiParam(value = "ID value for the bar you want to create the new person for") @PathVariable("barId") Long barId,
            @Valid @RequestBody PersonRequest personRequest
    ) throws NotFoundException {
        Person person = this.PERSON_SERVICE.createNewPerson(barId, personRequest);
        return new ResponseEntity<>(convertToPersonResponse(person),  HttpStatus.CREATED);
    }

    @PatchMapping("/{personId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Updates the person of bar",
            notes = "Provide id of bar and person to update the person with the information from the request body",
            response = PersonResponse.class
    )
    public ResponseEntity<PersonResponse> updatePerson(
            @ApiParam(value = "ID value for the bar you want to update the person from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the person you want to update") @PathVariable("personId") Long personId,
            @RequestBody PersonRequest personRequest)
            throws NotFoundException {
        Person person = this.PERSON_SERVICE.updatePerson(barId, personId, personRequest);
        return new ResponseEntity<>(convertToPersonResponse(person), HttpStatus.OK);
    }

    @DeleteMapping("/{personId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Deletes the person from bar",
            notes = "Provide id of bar and person to delete the person from the bar"
    )
    public ResponseEntity<Void> deletePerson(
            @ApiParam(value = "ID value for the bar you want to delete the person from")  @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the bar you want to delete")  @PathVariable("personId") Long personId)
            throws NotFoundException {
        this.PERSON_SERVICE.deletePersonFromBar(barId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
