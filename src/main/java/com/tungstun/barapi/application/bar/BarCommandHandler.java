package com.tungstun.barapi.application.bar;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bar.command.CreateBar;
import com.tungstun.barapi.application.bar.command.DeleteBar;
import com.tungstun.barapi.application.bar.command.UpdateBar;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
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

    public UUID handle(CreateBar command) {
        User user = (User) userQueryHandler.loadUserByUsername(command.ownerUsername());
        boolean exists = barRepository.findAllById(user.getAuthorizations().keySet())
                .stream()
                .anyMatch(bar -> bar.getDetails().getName().equals(command.name()));
        if (exists) {
            throw new DuplicateRequestException("User already owns bar with name: " + command.name());
        }
        Person owner = new PersonBuilder(command.ownerUsername())
                .setUser(user)
                .build();
        Bar bar = new BarBuilder(command.name())
                .setAddress(command.address())
                .setMail(command.mail())
                .setPhoneNumber(command.phoneNumber())
                .setPeople(List.of(owner))
                .build();

        user.newBarAuthorization(bar.getId(), owner);
        userRepository.update(user);
        return barRepository.save(bar).getId();
    }

    public UUID handle(UpdateBar command) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(command.barId()));
        bar.getDetails().setAddress(command.address());
        bar.getDetails().setName(command.name());
        bar.getDetails().setMail(command.mail());
        bar.getDetails().setPhoneNumber(command.phoneNumber());
        return barRepository.save(bar).getId();
    }

    public void handle(DeleteBar command) {
        barRepository.delete(command.barId());
    }
}
