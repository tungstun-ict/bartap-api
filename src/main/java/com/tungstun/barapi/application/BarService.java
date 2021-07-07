package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import com.tungstun.security.application.UserService;
import com.tungstun.security.data.model.User;
import com.tungstun.security.data.model.UserBarAuthorization;
import com.tungstun.security.data.model.UserRole;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BarService {
    private final SpringBarRepository SPRING_BAR_REPOSITORY;
    private final UserService USER_SERVICE;

    public BarService(SpringBarRepository springBarRepository, UserService userService) {
        this.SPRING_BAR_REPOSITORY = springBarRepository;
        this.USER_SERVICE = userService;
    }

    public List<Bar> getAllBars() {
        return this.SPRING_BAR_REPOSITORY.findAll();
    }

    public List<Bar> getAllBarOwnerBars(String username) {
        User user = (User) this.USER_SERVICE.loadUserByUsername(username);
        Set<Long> ownedBarIds = user.getAuthoritiesMap().keySet();
        return getAllBars()
                .stream()
                .filter(bar -> ownedBarIds.contains(bar.getId()))
                .collect(Collectors.toList());
    }

    public Bar getBar(Long id) throws NotFoundException {
        return this.SPRING_BAR_REPOSITORY.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Bar with id %s doesn't exist", id)));
    }

    public Bar addBar(BarRequest barRequest, String ownerUsername) {
        User user = (User) this.USER_SERVICE.loadUserByUsername(ownerUsername);
        checkIfBarExistsForPerson(barRequest.name, user);
        Bar bar = new BarBuilder()
                .setAddress(barRequest.address)
                .setName(barRequest.name)
                .setMail(barRequest.mail)
                .setPhoneNumber(barRequest.phoneNumber)
                .build();
        bar = this.SPRING_BAR_REPOSITORY.save(bar);
        user.addUserBarAuthorizations(new UserBarAuthorization(bar, user, UserRole.ROLE_BAR_OWNER));
        Person owner = new PersonBuilder()
                .setName(ownerUsername)
                .setUser(user)
                .build();
        bar.addUser(owner);
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    private void checkIfBarExistsForPerson(String name, User user) {
        SPRING_BAR_REPOSITORY.findBarByDetails_Name(name).ifPresent(bar -> {
           if ( bar.getUsers().stream()
                   .anyMatch(barUser -> barUser.getUser().equals(user))) {
               throw new DuplicateRequestException(String.format("Bar with name %s already exists", name));
           }
        });
    }

    public Bar updateBar(Long id, BarRequest barRequest) throws NotFoundException {
        Bar bar = getBar(id);
        bar.getDetails().setAddress(barRequest.address);
        bar.getDetails().setMail(barRequest.mail);
        bar.getDetails().setName(barRequest.name);
        bar.getDetails().setPhoneNumber(barRequest.phoneNumber);
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    public Bar saveBar(Bar bar) {
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    public void deleteBar(Long id) {
        this.SPRING_BAR_REPOSITORY.deleteById(id);
    }
}
