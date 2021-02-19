package com.tungstun.security.application;

import com.tungstun.security.data.SpringUserRepository;
import com.tungstun.security.data.User;
import com.tungstun.security.data.UserRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final SpringUserRepository SPRING_USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;

    public UserService(SpringUserRepository springUserRepository, PasswordEncoder passwordEncoder) {
        this.SPRING_USER_REPOSITORY = springUserRepository;
        this.PASSWORD_ENCODER = passwordEncoder;
    }

    public void registerBarOwner(String username, String password) {
        String encodedPassword = this.PASSWORD_ENCODER.encode(password);
        User user = new User(username, encodedPassword, UserRole.ROLE_BAR_OWNER);
        //todo: Bar direct automatisch aanmaken, of nog niet en later zelf?
        this.SPRING_USER_REPOSITORY.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.SPRING_USER_REPOSITORY.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with the usename '%s'", username)));
    }
}
