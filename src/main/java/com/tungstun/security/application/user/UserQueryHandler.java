package com.tungstun.security.application.user;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.ListOwnedBars;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.exception.UserNotFoundException;
import com.tungstun.security.application.user.query.GetUser;
import com.tungstun.security.application.user.query.GetUserSummary;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.domain.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserQueryHandler implements UserDetailsService {
    private final UserRepository userRepository;
    private final BarQueryHandler barQueryHandler;

    public UserQueryHandler(UserRepository userRepository, BarQueryHandler barQueryHandler) {
        this.userRepository = userRepository;
        this.barQueryHandler = barQueryHandler;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username '%s' was not found", username)));
    }
    public User handle(GetUser query) throws UserNotFoundException {
        return userRepository.findById(query.userId())
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id '%s' was not found", query.userId())));
    }

    public User handle(GetUserSummary query) throws UserNotFoundException {
        User user = handle(new GetUser(query.userId()));

        barQueryHandler.handle(new ListOwnedBars(query.ownerUsername()))
                .parallelStream()
                .map(Bar::getPeople)
                .flatMap(List::stream)
                .filter(person -> person.getUser().equals(user))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + query.userId()));

        return user;
    }
}
