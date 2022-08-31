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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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

    @GetMapping(path = "/bars/{barId}/people/{personId}")
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

    @PostMapping(path = "/bars/{barId}/people/{personId}/connect")
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
        String token = personCommandHandler.handle(new CreatePersonConnectionToken(barId, personId));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setAll(Map.of("connect_token", token));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping(path = "/connect-user")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasPermission(#barId, {})")
    @ApiOperation(
            value = "Gets connection token of person of bar",
            notes = "Provide id of bar and person to get a connection token that can be used to connect the person to a user",
            response = PersonResponse.class
    )
    public void connectUserToPerson(
            @ApiIgnore Authentication authentication,
            @RequestBody @Valid ConnectUserToPersonRequest request
    ) throws EntityNotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userQueryHandler.loadUserByUsername(userDetails.getUsername());

      personCommandHandler.handle(new ConnectUserToPerson(user.getUsername(), request.token()));
    }

    @PostMapping("/bars/{barId}/people")
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

    @PutMapping("/bars/{barId}/people/{personId}")
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

    @DeleteMapping("/bars/{barId}/people/{personId}")
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
