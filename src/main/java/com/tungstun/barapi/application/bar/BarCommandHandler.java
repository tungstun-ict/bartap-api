package com.tungstun.barapi.application.bar;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bar.command.CreateBar;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import com.tungstun.security.application.user.UserQueryHandler;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BarCommandHandler {
    private final BarQueryHandler barQueryHandler;
    private final BarRepository barRepository;
    private final UserRepository userRepository;
    private final UserQueryHandler userQueryHandler;

    public BarCommandHandler(BarQueryHandler barQueryHandler, BarRepository springBarRepository, UserRepository userRepository, UserQueryHandler userQueryHandler) {
        this.barQueryHandler = barQueryHandler;
        this.barRepository = springBarRepository;
        this.userRepository = userRepository;
        this.userQueryHandler = userQueryHandler;
    }

    public UUID addBar(CreateBar command) {
        User user = (User) userQueryHandler.loadUserByUsername(command.ownerUsername());
        boolean exists = barRepository.findAllById(user.getAuthorizations().keySet())
                .stream()
                .anyMatch(bar -> bar.getDetails().getName().equals(command.name()));
        if (exists) {
            throw new DuplicateRequestException("User already owns bar with name: " + command.name());
        }

        Bar bar = new BarBuilder(command.name())
                .setAddress(command.address())
                .setMail(command.mail())
                .setPhoneNumber(command.phoneNumber())
                .setPeople(
                        List.of(new PersonBuilder(command.ownerUsername())
                                .setUser(user)
                                .build())
                )
                .build();

        user.newBarAuthorization(bar.getId());
        userRepository.update(user);
        return barRepository.save(bar).getId();
    }
// Querys one when can be multiple
//    private void checkIfBarExistsForPerson(String name, User user) {
//        barRepository.findBarByDetails_Name(name)
//                .ifPresent(bar -> {
//                    bar.getPeople()
//                            .stream()
//                            .anyMatch(barUser -> barUser.getUser().equals(user));
//                    if () {
//                        throw new DuplicateRequestException(String.format("Bar with name %s already exists", name));
//                    }
//                });
//    }

    public UUID updateBar(UUID barId, BarRequest barRequest) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(barId));
        bar.getDetails().setAddress(barRequest.address);
        bar.getDetails().setMail(barRequest.mail);
        bar.getDetails().setName(barRequest.name);
        bar.getDetails().setPhoneNumber(barRequest.phoneNumber);
        return barRepository.save(bar).getId();
    }

    public void deleteBar(UUID id) {
        barRepository.delete(id);
    }
}
