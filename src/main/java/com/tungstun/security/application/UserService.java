package com.tungstun.security.application;

import com.tungstun.security.data.SpringUserRepository;
import com.tungstun.security.data.User;
import com.tungstun.security.data.UserRole;
import com.tungstun.security.presentation.dto.request.LoginRequest;
import com.tungstun.security.presentation.dto.request.UserRegistrationRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final SpringUserRepository SPRING_USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration-in-ms}")
    private Integer jwtExpirationInMs;

    public UserService(SpringUserRepository springUserRepository,
                       PasswordEncoder passwordEncoder
    ) {
        this.SPRING_USER_REPOSITORY = springUserRepository;
        this.PASSWORD_ENCODER = passwordEncoder;
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
        return String.format("Bearer: %s", generateJWT(user));
    }

    private String generateJWT(User user) {
        List<String> roles = extractUserRoles(user);
        byte[] signingKey = this.jwtSecret.getBytes();
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256)
                .setHeaderParam("type", "JWT")
                .setIssuer("com-tungstun-bar-api")
                .setAudience("com-tungstun-bar-api")
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .claim("roles", roles)
                .compact();
    }

    private List<String> extractUserRoles(User user) {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.SPRING_USER_REPOSITORY.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with the usename '%s'", username)));
    }
}
