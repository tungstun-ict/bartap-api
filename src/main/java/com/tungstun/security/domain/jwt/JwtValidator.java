package com.tungstun.security.domain.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tungstun.exception.NotAuthenticatedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtValidator {
    private final JwtCredentials credentials;

    public JwtValidator(JwtCredentials credentials) {
        this.credentials = credentials;
    }

    private DecodedJWT verify(String token, JWTVerifier verifier) {
        if (token == null) {
            System.out.println("demodemo");
            System.out.println(token);
            throw new NotAuthenticatedException("Invalid token");
        }

        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new NotAuthenticatedException("Invalid token", e);
        }
    }

    public DecodedJWT verifyAccessToken(String token) {
        JWTVerifier verifier = JWT.require(credentials.algorithm())
                .withIssuer(credentials.getJwtIssuer())
                .withAudience(credentials.getJwtAudience())
                .withClaimPresence("client_id")
                .withClaimPresence("authorizations")
                .acceptLeeway(1)
                .build();
        return verify(token, verifier);
    }

    public DecodedJWT verifyAccessTokenSignature(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Algorithm algorithm = credentials.algorithm();

            if (!algorithm.getName().equals(decodedJWT.getAlgorithm())) {
                throw new NotAuthenticatedException("Invalid token");
            }

            algorithm.verify(decodedJWT);

            if (decodedJWT.getAudience().equals(List.of(credentials.getJwtAudience())) ||
                    decodedJWT.getIssuer().equals(credentials.getJwtIssuer()) ||
                    decodedJWT.getClaim("client_id").isNull() ||
                    decodedJWT.getClaim("authorizations").isNull()) {
                return decodedJWT;
            }
            throw new NotAuthenticatedException("Invalid token");

        } catch (SignatureVerificationException | JWTDecodeException e) {
            throw new NotAuthenticatedException("Invalid token");
        }
    }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(credentials.algorithm())
                .withIssuer(credentials.getJwtIssuer())
                .withAudience(credentials.getJwtAudience())
                .acceptLeeway(1)
                .build();
        return verify(token, verifier);
    }
}
