package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.PersonService;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.presentation.dto.converter.PersonConverter;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import com.tungstun.barapi.presentation.dto.response.PersonResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bars/{barId}/people")
public class PersonController {
    private final PersonService personService;
    private final PersonConverter converter;

    public PersonController(PersonService personService, PersonConverter converter) {
        this.personService = personService;
        this.converter = converter;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds all people of bar",
            notes = "Provide id of bar to look up all people that are linked to the bar",
            response = PersonResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<PersonResponse>> getAllPeopleOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve people from") @PathVariable Long barId
    ) throws EntityNotFoundException {
        List<Person> allPeople = this.personService.getAllPeopleOfBar(barId);
        return new ResponseEntity<>(converter.convertAll(allPeople), HttpStatus.OK);
    }

    @GetMapping(path = "/{personId}")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds person of bar",
            notes = "Provide id of bar and person to look up a specific person of the bar",
            response = PersonResponse.class
    )
    public ResponseEntity<PersonResponse> getPersonOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the person from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the person you want to retrieve") @PathVariable("personId") Long personId
    ) throws EntityNotFoundException {
        Person person = this.personService.getPersonOfBar(barId, personId);
        return new ResponseEntity<>(converter.convert(person),  HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Creates person for bar",
            notes = "Provide id of bar to create a new person with the information from the request body for the bar",
            response = PersonResponse.class
    )
    public ResponseEntity<PersonResponse> createNewPersonForBar(
            @ApiParam(value = "ID value for the bar you want to create the new person for") @PathVariable("barId") Long barId,
            @Valid @RequestBody PersonRequest personRequest
    ) throws EntityNotFoundException {
        Person person = this.personService.createNewPerson(barId, personRequest);
        return new ResponseEntity<>(converter.convert(person),  HttpStatus.CREATED);
    }

    @PutMapping("/{personId}")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Updates the person of bar",
            notes = "Provide id of bar and person to update the person with the information from the request body",
            response = PersonResponse.class
    )
    public ResponseEntity<PersonResponse> updatePerson(
            @ApiParam(value = "ID value for the bar you want to update the person from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the person you want to update") @PathVariable("personId") Long personId,
            @Valid @RequestBody PersonRequest personRequest)
            throws EntityNotFoundException {
        Person person = this.personService.updatePerson(barId, personId, personRequest);
        return new ResponseEntity<>(converter.convert(person), HttpStatus.OK);
    }

    @DeleteMapping("/{personId}")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Deletes the person from bar",
            notes = "Provide id of bar and person to delete the person from the bar"
    )
    public ResponseEntity<Void> deletePerson(
            @ApiParam(value = "ID value for the bar you want to delete the person from")  @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the bar you want to delete")  @PathVariable("personId") Long personId)
            throws EntityNotFoundException {
        this.personService.deletePersonFromBar(barId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
