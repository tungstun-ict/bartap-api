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
    private final SpringUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final JwtValidator jwtValidator;
    private final RegistrationValidator registrationValidator;

    public UserService(SpringUserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtGenerator jwtGenerator,
                       JwtValidator jwtValidator,
                       RegistrationValidator registrationValidator
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.jwtValidator = jwtValidator;
        this.registrationValidator = registrationValidator;
    }

    public void registerBarOwner(UserRegistrationRequest userRegistrationRequest) throws AccountException {
        this.registrationValidator.validateRegistrationDetails(userRegistrationRequest);
        String encodedPassword = this.passwordEncoder.encode(userRegistrationRequest.password);
        User user = new User(
                userRegistrationRequest.username,
                encodedPassword,
                userRegistrationRequest.mail,
                userRegistrationRequest.firstName,
                userRegistrationRequest.lastName,
                new ArrayList<>()
        );
        this.userRepository.save(user);
    }


    public Map<String, String> loginUser(LoginRequest loginRequest) throws LoginException {
        Map<String, String> authTokenMap = new HashMap<>();
        User user = (User) loadUserByMailOrUsername(loginRequest.userIdentification);
        attemptLogin(loginRequest.password, user.getPassword());
        authTokenMap.put("token_type", "bearer");
        authTokenMap.put("access_token", this.jwtGenerator.generateAccessToken(user));
        authTokenMap.put("refresh_token", this.jwtGenerator.generateRefreshToken());
        return authTokenMap;
    }

    private void attemptLogin(String providedPassword, String storedPassword) throws LoginException {
        if (!isMatchingPassword(providedPassword, storedPassword)) throw new LoginException("Incorrect password");
    }

    private boolean isMatchingPassword(String providedPassword, String hashedPassword) {
        return this.passwordEncoder.matches(providedPassword, hashedPassword);
    }

    public Map<String, String> refreshUserToken(RefreshTokenRequest refreshTokenRequest) {
        this.jwtValidator.validateAccessJwt(refreshTokenRequest.accessToken);
        this.jwtValidator.validateRefreshJwt(refreshTokenRequest.refreshToken);
        String accessToken = this.jwtGenerator.refreshAccessTokenFromAccessToken(refreshTokenRequest.accessToken);
        Map<String, String> authTokenMap = new HashMap<>();
        authTokenMap.put("access_token", accessToken);
        return authTokenMap;
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
