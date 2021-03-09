package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
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

    public List<Bar> getAllBars() throws NotFoundException{
        List<Bar> bars = this.SPRING_BAR_REPOSITORY.findAll();
        if (bars.isEmpty()) throw new NotFoundException("There are no bars available");
        return bars;
    }

    public List<Bar> getAllBarOwnerBars(String username) throws NotFoundException {
        User user = (User) this.USER_SERVICE.loadUserByUsername(username);
        Set<Long> ownedBarIds = user.getAuthoritiesMap().keySet();
        return getAllBars()
                .stream()
                .filter(bar -> ownedBarIds.contains(bar.getId()))
                .collect(Collectors.toList());
    }

    public Bar getBar(Long id) throws NotFoundException {
        return this.SPRING_BAR_REPOSITORY.findById(id)
         .orElseThrow(() -> new NotFoundException(
                 String.format("Bar with id %s doesn't exist", id)) );
    }

    public Bar addBar(BarRequest barRequest, String ownerUsername) {
        checkIfBarExists(barRequest.name);
        Bar bar = new BarBuilder()
                .setAdres(barRequest.address)
                .setName(barRequest.name)
                .setMail(barRequest.mail)
                .setPhoneNumber(barRequest.phoneNumber)
                .build();
        bar = this.SPRING_BAR_REPOSITORY.save(bar);
        User user = (User) this.USER_SERVICE.loadUserByUsername(ownerUsername);
        user.addUserBarAuthorizations(new UserBarAuthorization(bar.getId(), user.getId(), UserRole.ROLE_BAR_OWNER));
        Bartender bartender = new Bartender(ownerUsername);
        bartender.setUser(user);
        bar.addUser(bartender);
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    private void checkIfBarExists(String name){
        SPRING_BAR_REPOSITORY.findBarByName(name).ifPresent(error ->{
            throw new DuplicateRequestException(
                    String.format("Bar with name %s already exists", name)); });
    }

    public Bar updateBar(Long id, BarRequest barRequest) throws NotFoundException {
        Bar bar = getBar(id);
        bar.setAddress(barRequest.address);
        bar.setMail(barRequest.mail);
        bar.setName(barRequest.name);
        bar.setPhoneNumber(barRequest.phoneNumber);
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    public Bar saveBar(Bar bar){ return this.SPRING_BAR_REPOSITORY.save(bar); }

    public void deleteBar(Long id) throws NotFoundException { this.SPRING_BAR_REPOSITORY.delete(getBar(id)); }
}
