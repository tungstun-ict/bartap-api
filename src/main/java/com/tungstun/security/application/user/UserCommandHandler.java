package com.tungstun.security.application.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tungstun.security.application.user.command.*;
import com.tungstun.security.application.user.query.GetUser;
import com.tungstun.security.domain.jwt.JwtTokenGenerator;
import com.tungstun.security.domain.jwt.JwtValidator;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.domain.user.UserRepository;
import com.tungstun.security.util.account.RegistrationValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
public class UserCommandHandler {
    private final UserQueryHandler userQueryHandler;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtValidator jwtValidator;
    private final RegistrationValidator registrationValidator;

    public UserCommandHandler(UserQueryHandler userQueryHandler, UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              JwtTokenGenerator jwtTokenGenerator,
                              JwtValidator jwtValidator,
                              RegistrationValidator registrationValidator
    ) {
        this.userQueryHandler = userQueryHandler;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtValidator = jwtValidator;
        this.registrationValidator = registrationValidator;
    }

    public UUID registerUser(RegisterUser command) throws AccountException {
        registrationValidator.validateRegistrationDetails(command);
        String encodedPassword = passwordEncoder.encode(command.password());
        return userRepository.save(new User(
                UUID.randomUUID(),
                command.username(),
                encodedPassword,
                command.mail().strip(),
                command.firstName(),
                command.lastName(),
                command.phoneNumber(),
                new ArrayList<>()
        )).getId();
    }

    public UUID handle(UpdateUser command) {
        User user = userQueryHandler.handle(new GetUser(command.userId()));
        if (command.firstName() != null) user.setFirstName(command.firstName());
        if (command.lastName() != null) user.setLastName(command.lastName());
        if (command.phoneNumber() != null) user.setPhoneNumber(command.phoneNumber());
        return userRepository.save(user).getId();
    }

    public void handle(DeleteUser command) {
        userRepository.delete(command.id());
    }

    public Map<String, String> handle(LogIn command) throws LoginException {
        User user = (User) userQueryHandler.loadUserByUsername(command.username());
        user.canAuthenticate();
        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new LoginException("Incorrect password");
        }

        return Map.of(
                "token_type", "bearer",
                "access_token", jwtTokenGenerator.createAccessToken(user),
                "refresh_token", jwtTokenGenerator.createRefreshToken()
        );
    }

    public Map<String, String> handle(RefreshAccessToken command) {
        jwtValidator.verifyToken(command.refreshToken());
        DecodedJWT accessTokenInfo = jwtValidator.verifyAccessTokenSignature(command.accessToken());
        User userDetails = (User) userQueryHandler.loadUserByUsername(accessTokenInfo.getSubject());
        String newAccessToken = jwtTokenGenerator.createAccessToken(userDetails);
        return Collections.singletonMap("access_token", newAccessToken);
    }
}
