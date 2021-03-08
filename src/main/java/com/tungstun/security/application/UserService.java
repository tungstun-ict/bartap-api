package com.tungstun.security.application;

import com.tungstun.security.data.User;
import com.tungstun.security.data.UserRole;
import com.tungstun.security.data.repository.SpringUserRepository;
import com.tungstun.security.presentation.dto.request.LoginRequest;
import com.tungstun.security.presentation.dto.request.UserRegistrationRequest;
import com.tungstun.security.util.jwt.JwtGenerator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class UserService implements UserDetailsService {
    private final SpringUserRepository SPRING_USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;
    private final JwtGenerator JWT_GENERATOR;

    public UserService(SpringUserRepository springUserRepository,
                       PasswordEncoder passwordEncoder,
                       JwtGenerator jwtGenerator
    ) {
        this.SPRING_USER_REPOSITORY = springUserRepository;
        this.PASSWORD_ENCODER = passwordEncoder;
        this.JWT_GENERATOR = jwtGenerator;
    }

    public void registerBarOwner(UserRegistrationRequest userRegistrationRequest) {
        String encodedPassword = this.PASSWORD_ENCODER.encode(userRegistrationRequest.password);
        User user = new User(userRegistrationRequest.username, encodedPassword, UserRole.ROLE_BAR_OWNER);
        this.SPRING_USER_REPOSITORY.save(user);
    }

    public String loginUser(LoginRequest loginRequest) throws LoginException {
        User user = (User) loadUserByUsername(loginRequest.username);
        attemptLogin(loginRequest.password, user.getPassword());
        return generateAuthToken(user);
    }

    private void attemptLogin(String providedPassword, String storedPassword) throws LoginException {
        if (!isMatchingPassword(providedPassword, storedPassword)) throw new LoginException("Incorrect password");
    }

    private boolean isMatchingPassword(String providedPassword, String hashedPassword) {
        return this.PASSWORD_ENCODER.matches(providedPassword, hashedPassword);
    }

    private String generateAuthToken(User user) {
        return String.format("Bearer %s", this.JWT_GENERATOR.generate(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.SPRING_USER_REPOSITORY.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with the usename '%s'", username)));
    }
}
