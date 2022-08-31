package com.tungstun.barapi.port.web.person;

import com.tungstun.barapi.application.person.PersonCommandHandler;
import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.command.CreatePerson;
import com.tungstun.barapi.application.person.command.CreatePersonConnectionToken;
import com.tungstun.barapi.application.person.command.DeletePerson;
import com.tungstun.barapi.application.person.command.UpdatePerson;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.application.person.query.ListPeopleOfBar;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.port.web.person.converter.PersonConverter;
import com.tungstun.barapi.port.web.person.request.CreatePersonRequest;
import com.tungstun.barapi.port.web.person.request.UpdatePersonRequest;
import com.tungstun.barapi.port.web.person.response.PersonResponse;
import com.tungstun.common.response.UuidResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
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
        Person person = personQueryHandler.handle(new GetPerson(barId, personId));
        return converter.convert(person);
    }

    @GetMapping(path = "/{personId}/connect")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Gets connection token of person of bar",
            notes = "Provide id of bar and person to get a connection token that can be used to connect the person to a user",
            response = PersonResponse.class
    )
    public ResponseEntity<Void> getPersonConnectionToken(
            @ApiParam(value = "ID value for the bar you want to get the person's connection token from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the person you want to retrieve the connection token from") @PathVariable("personId") UUID personId
    ) throws EntityNotFoundException {
        String token  = personCommandHandler.handle(new CreatePersonConnectionToken(barId, personId));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setAll(Map.of("connect_token", token));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Creates person for bar",
            notes = "Provide categoryId of bar to create a new person with the information from the request body for the bar",
            response = PersonResponse.class
    )
    public UuidResponse createNewPersonForBar(
            @ApiParam(value = "ID value for the bar you want to create the new person for") @PathVariable("barId") UUID barId,
            @Valid @RequestBody CreatePersonRequest request
    ) throws EntityNotFoundException {
        CreatePerson command = new CreatePerson(barId, request.name());
        return new UuidResponse(personCommandHandler.handle(command));
    }

    @PutMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Updates the person of bar",
            notes = "Provide categoryId of bar and person to update the person with the information from the request body",
            response = PersonResponse.class
    )
    public UuidResponse updatePerson(
            @ApiParam(value = "ID value for the bar you want to update the person from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the person you want to update") @PathVariable("personId") UUID personId,
            @Valid @RequestBody UpdatePersonRequest request
    ) throws EntityNotFoundException {
        UpdatePerson command = new UpdatePerson(barId, personId, request.name());
        return new UuidResponse(personCommandHandler.handle(command));
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
        DeletePerson command = new DeletePerson(personId);
        personCommandHandler.handle(command);
    }
}
