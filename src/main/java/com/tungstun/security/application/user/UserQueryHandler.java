package com.tungstun.security.application.user;

import com.tungstun.barapi.exceptions.UserNotFoundException;
import com.tungstun.security.domain.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserQueryHandler implements UserDetailsService {
    private final UserRepository userRepository;

    public UserQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username '%s' was not found", username)));
    }
}
