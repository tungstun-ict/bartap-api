package com.tungstun.security.application;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tungstun.security.domain.jwt.JwtTokenGenerator;
import com.tungstun.security.domain.jwt.JwtValidator;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.domain.user.UserRepository;
import com.tungstun.security.presentation.dto.request.LoginRequest;
import com.tungstun.security.presentation.dto.request.RefreshTokenRequest;
import com.tungstun.security.presentation.dto.request.UserRegistrationRequest;
import com.tungstun.security.util.account.RegistrationValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtValidator jwtValidator;
    private final RegistrationValidator registrationValidator;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenGenerator jwtTokenGenerator,
                       JwtValidator jwtValidator,
                       RegistrationValidator registrationValidator
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtValidator = jwtValidator;
        this.registrationValidator = registrationValidator;
    }

    public void registerUser(UserRegistrationRequest userRegistrationRequest) throws AccountException {
        this.registrationValidator.validateRegistrationDetails(userRegistrationRequest);
        String encodedPassword = this.passwordEncoder.encode(userRegistrationRequest.password);
        User user = new User(
                userRegistrationRequest.username,
                encodedPassword,
                userRegistrationRequest.mail.strip(),
                userRegistrationRequest.firstName,
                userRegistrationRequest.lastName,
                userRegistrationRequest.phoneNumber,
                new ArrayList<>()
        );
        this.userRepository.save(user);
    }

    public void handle(UserRegistrationRequest command) {
        User user = (User) loadUserByUsername(command.username);
        if (command.firstName != null) user.setFirstName(command.firstName);
        if (command.lastName != null) user.setLastName(command.lastName);
        if (command.phoneNumber != null) user.setPhoneNumber(command.phoneNumber);
        userRepository.save(user);
    }

    public Map<String, String> loginUser(LoginRequest loginRequest) throws LoginException {
        User user = (User) loadUserByMailOrUsername(loginRequest.userIdentification);
        user.canAuthenticate();
        if (!passwordEncoder.matches(loginRequest.password, user.getPassword())) {
            throw new LoginException("Incorrect password");
        }

        return Map.of(
                "token_type", "bearer",
                "access_token", jwtTokenGenerator.createAccessToken(user),
                "refresh_token", jwtTokenGenerator.createRefreshToken()
        );
    }

    public Map<String, String> refreshUserToken(RefreshTokenRequest refreshTokenRequest) {
        jwtValidator.verifyRefreshToken(refreshTokenRequest.refreshToken);
        DecodedJWT accessTokenInfo = jwtValidator.verifyAccessToken(refreshTokenRequest.accessToken);
        User userDetails = (User) loadUserByUsername(accessTokenInfo.getSubject());
        String newAccessToken = jwtTokenGenerator.createAccessToken(userDetails);
        return Collections.singletonMap("access_token", newAccessToken);
    }

    public UserDetails loadUserByMailOrUsername(String username) {
        return this.userRepository.findByMailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with the username '%s'", username)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with the username '%s'", username)));
    }
}
