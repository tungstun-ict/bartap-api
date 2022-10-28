package com.tungstun.barapi.port.web.person.converter;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.port.web.person.response.PersonResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PersonConverter {
    public PersonConverter() {

    }

    public PersonResponse convert(Person person) {
        UUID userId = person.getUser() != null
                ? person.getUser().getId()
                : null;
        return new PersonResponse(
                person.getId(),
                person.getName(),
                userId
        );
    }

    public List<PersonResponse> convertAll(List<Person> people) {
        return people.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
