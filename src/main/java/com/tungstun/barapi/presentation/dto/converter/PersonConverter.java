package com.tungstun.barapi.presentation.dto.converter;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.presentation.dto.response.PersonResponse;
import com.tungstun.security.presentation.dto.converter.UserConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonConverter {
    private final UserConverter userConverter;

    public PersonConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public PersonResponse convert(Person person) {
        PersonResponse response =  new PersonResponse();
        response.setId(person.getId());
        response.setName(person.getName());
        if (person.getUser() != null){
            response.setUser(userConverter.convertToSummary(person.getUser()));
        }
        return response;
    }

    public List<PersonResponse> convertAll(List<Person> people) {
        return people.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
