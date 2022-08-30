package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.person.PersonCommandHandler;
import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.command.CreatePerson;
import com.tungstun.barapi.application.person.command.UpdatePerson;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.application.person.query.ListPeopleOfBar;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.presentation.dto.converter.PersonConverter;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import com.tungstun.barapi.presentation.dto.response.PersonResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bars/{barId}/people")
public class PersonController {
    private final PersonQueryHandler personQueryHandler;
    private final PersonCommandHandler personCommandHandler;
    private final PersonConverter converter;

    public PersonController(PersonQueryHandler personQueryHandler, PersonCommandHandler personCommandHandler, PersonConverter converter) {
        this.personQueryHandler = personQueryHandler;
        this.personCommandHandler = personCommandHandler;
        this.converter = converter;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds all people of bar",
            notes = "Provide categoryId of bar to look up all people that are linked to the bar",
            response = PersonResponse.class,
            responseContainer = "List"
    )
    public List<PersonResponse> getAllPeopleOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve people from") @PathVariable UUID barId
    ) throws EntityNotFoundException {
        List<Person> allPeople = personQueryHandler.handle(new ListPeopleOfBar(barId));
        return converter.convertAll(allPeople);
    }

    @GetMapping(path = "/{personId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds person of bar",
            notes = "Provide categoryId of bar and person to look up a specific person of the bar",
            response = PersonResponse.class
    )
    public PersonResponse getPersonOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the person from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the person you want to retrieve") @PathVariable("personId") UUID personId
    ) throws EntityNotFoundException {
        Person person = personQueryHandler.handle(new GetPerson(personId, barId));
        return converter.convert(person);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Creates person for bar",
            notes = "Provide categoryId of bar to create a new person with the information from the request body for the bar",
            response = PersonResponse.class
    )
    public UUID createNewPersonForBar(
            @ApiParam(value = "ID value for the bar you want to create the new person for") @PathVariable("barId") UUID barId,
            @Valid @RequestBody PersonRequest personRequest
    ) throws EntityNotFoundException {
        CreatePerson command = new CreatePerson(barId, personRequest.name);
        return personCommandHandler.createNewPerson(command);
    }

    @PutMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Updates the person of bar",
            notes = "Provide categoryId of bar and person to update the person with the information from the request body",
            response = PersonResponse.class
    )
    public UUID updatePerson(
            @ApiParam(value = "ID value for the bar you want to update the person from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the person you want to update") @PathVariable("personId") UUID personId,
            @Valid @RequestBody PersonRequest personRequest
    ) throws EntityNotFoundException {
        UpdatePerson command = new UpdatePerson(barId, personId, personRequest.name);
        return personCommandHandler.updatePerson(command);
    }

    @DeleteMapping("/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Deletes the person from bar",
            notes = "Provide categoryId of bar and person to delete the person from the bar"
    )
    public void deletePerson(
            @ApiParam(value = "ID value for the bar you want to delete the person from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the bar you want to delete") @PathVariable("personId") UUID personId
    ) throws EntityNotFoundException {
        personCommandHandler.deletePersonFromBar(barId, personId);
    }
}
