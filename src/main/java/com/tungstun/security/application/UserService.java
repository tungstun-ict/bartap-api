package com.tungstun.security.application;

import com.tungstun.security.data.model.User;
import com.tungstun.security.data.repository.SpringUserRepository;
import com.tungstun.security.presentation.dto.request.LoginRequest;
import com.tungstun.security.presentation.dto.request.RefreshTokenRequest;
import com.tungstun.security.presentation.dto.request.UserRegistrationRequest;
import com.tungstun.security.util.account.RegistrationValidator;
import com.tungstun.security.util.jwt.JwtGenerator;
import com.tungstun.security.util.jwt.JwtValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {
    private final SpringUserRepository SPRING_USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;
    private final JwtGenerator JWT_GENERATOR;
    private final JwtValidator JWT_VALIDATOR;
    private final RegistrationValidator REGISTRATION_VALIDATOR;

    public UserService(SpringUserRepository springUserRepository,
                       PasswordEncoder passwordEncoder,
                       JwtGenerator jwtGenerator,
                       JwtValidator jwtValidator,
                       RegistrationValidator registrationValidator
    ) {
        this.SPRING_USER_REPOSITORY = springUserRepository;
        this.PASSWORD_ENCODER = passwordEncoder;
        this.JWT_GENERATOR = jwtGenerator;
        this.JWT_VALIDATOR = jwtValidator;
        this.REGISTRATION_VALIDATOR = registrationValidator;
    }

    public void registerBarOwner(UserRegistrationRequest userRegistrationRequest) throws AccountException {
        this.REGISTRATION_VALIDATOR.validateRegistrationDetails(userRegistrationRequest);
        String encodedPassword = this.PASSWORD_ENCODER.encode(userRegistrationRequest.password);
        User user = new User(
                userRegistrationRequest.username,
                encodedPassword,
                userRegistrationRequest.mail,
                userRegistrationRequest.firstName,
                userRegistrationRequest.lastName,
                new ArrayList<>()
        );
        this.SPRING_USER_REPOSITORY.save(user);
    }


    public Map<String, String> loginUser(LoginRequest loginRequest) throws LoginException {
        Map<String, String> authTokenMap = new HashMap<>();
        User user = (User) loadUserByMailOrUsername(loginRequest.userIdentification);
        attemptLogin(loginRequest.password, user.getPassword());
        authTokenMap.put("token_type", "bearer");
        authTokenMap.put("access_token", this.JWT_GENERATOR.generateAccessToken(user));
        authTokenMap.put("refresh_token", this.JWT_GENERATOR.generateRefreshToken());
        return authTokenMap;
    }

    private void attemptLogin(String providedPassword, String storedPassword) throws LoginException {
        if (!isMatchingPassword(providedPassword, storedPassword)) throw new LoginException("Incorrect password");
    }

    private boolean isMatchingPassword(String providedPassword, String hashedPassword) {
        return this.PASSWORD_ENCODER.matches(providedPassword, hashedPassword);
    }

    public Map<String, String> refreshUserToken(RefreshTokenRequest refreshTokenRequest) {
        this.JWT_VALIDATOR.validateAccessJwt(refreshTokenRequest.accessToken);
        this.JWT_VALIDATOR.validateRefreshJwt(refreshTokenRequest.refreshToken);
        String accessToken = this.JWT_GENERATOR.refreshAccessTokenFromAccessToken(refreshTokenRequest.accessToken);
        Map<String, String> authTokenMap = new HashMap<>();
        authTokenMap.put("access_token", accessToken);
        return authTokenMap;
    }

    public UserDetails loadUserByMailOrUsername(String username) {
        return this.SPRING_USER_REPOSITORY.findByMailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with the username '%s'", username)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.SPRING_USER_REPOSITORY.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with the username '%s'", username)));
    }
}
