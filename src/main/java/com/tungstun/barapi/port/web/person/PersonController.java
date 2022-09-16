package com.tungstun.barapi.port.web.person;

import com.tungstun.barapi.application.person.PersonCommandHandler;
import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.command.*;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.application.person.query.ListPeopleOfBar;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.port.web.person.converter.PersonConverter;
import com.tungstun.barapi.port.web.person.request.ConnectUserToPersonRequest;
import com.tungstun.barapi.port.web.person.request.CreatePersonRequest;
import com.tungstun.barapi.port.web.person.request.UpdatePersonRequest;
import com.tungstun.barapi.port.web.person.response.PersonResponse;
import com.tungstun.common.response.UuidResponse;
import com.tungstun.security.application.user.UserQueryHandler;
import com.tungstun.security.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PersonController {
    private final PersonQueryHandler personQueryHandler;
    private final PersonCommandHandler personCommandHandler;
    private final PersonConverter converter;
    private final UserQueryHandler userQueryHandler;

    public PersonController(PersonQueryHandler personQueryHandler, PersonCommandHandler personCommandHandler, PersonConverter converter, UserQueryHandler userQueryHandler) {
        this.personQueryHandler = personQueryHandler;
        this.personCommandHandler = personCommandHandler;
        this.converter = converter;
        this.userQueryHandler = userQueryHandler;
    }

    @GetMapping("/bars/{barId}/people")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds people of a bar",
            description = "Find all people of a bar with the given id",
            tags = "Person"
    )
    public List<PersonResponse> getAllPeopleOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId
    ) throws EntityNotFoundException {
        List<Person> allPeople = personQueryHandler.handle(new ListPeopleOfBar(barId));
        return converter.convertAll(allPeople);
    }

    @GetMapping(path = "/bars/{barId}/people/{personId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds person of a bar",
            description = "Find a person of a bar with the given id's",
            tags = "Person"
    )
    public PersonResponse getPersonOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId,
            @Parameter(description = "Id value of the person") @PathVariable("personId") UUID personId
    ) throws EntityNotFoundException {
        Person person = personQueryHandler.handle(new GetPerson(barId, personId));
        return converter.convert(person);
    }

    @PostMapping(path = "/bars/{barId}/people/{personId}/connect")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Creates a person connection token for a user",
            description = "Creates and returns a connection token a user can use to connect with a person/customer of a bar so long no other user is connected to the person",
            tags = "Person"
    )
    public ResponseEntity<Void> getPersonConnectionToken(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId,
            @Parameter(description = "Id value of the person") @PathVariable("personId") UUID personId
    ) throws EntityNotFoundException {
        String token = personCommandHandler.handle(new CreatePersonConnectionToken(barId, personId));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setAll(Map.of("connect_token", token));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping(path = "/connect-user")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Connects a user and a person",
            description = "Connects a user with a person using the person's connection token",
            tags = "Person"
    )
    public void connectUserToPerson(
            @Parameter(hidden = true) Authentication authentication,
            @RequestBody @Valid ConnectUserToPersonRequest request
    ) throws EntityNotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userQueryHandler.loadUserByUsername(userDetails.getUsername());

        personCommandHandler.handle(new ConnectUserToPerson(user.getUsername(), request.token()));
    }

    @PostMapping("/bars/{barId}/people")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Creates a person",
            description = "Create a new person for a bar with the given id",
            tags = "Person"
    )
    public UuidResponse createNewPersonForBar(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId,
            @Valid @RequestBody CreatePersonRequest request
    ) throws EntityNotFoundException {
        CreatePerson command = new CreatePerson(barId, request.name());
        return new UuidResponse(personCommandHandler.handle(command));
    }

    @PutMapping("/bars/{barId}/people/{personId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Updates a person of a bar",
            description = "Update a person of a bar with the given information",
            tags = "Person"
    )
    public UuidResponse updatePerson(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId,
            @Parameter(description = "Id value of the person") @PathVariable("personId") UUID personId,
            @Valid @RequestBody UpdatePersonRequest request
    ) throws EntityNotFoundException {
        UpdatePerson command = new UpdatePerson(barId, personId, request.name());
        return new UuidResponse(personCommandHandler.handle(command));
    }

    @DeleteMapping("/bars/{barId}/people/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Deletes a person from a bar",
            description = "Delete a person of a bar with the given id's",
            tags = "Person"
    )
    public void deletePerson(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId,
            @Parameter(description = "Id value of the person") @PathVariable("personId") UUID personId
    ) throws EntityNotFoundException {
        DeletePerson command = new DeletePerson(personId);
        personCommandHandler.handle(command);
    }
}
