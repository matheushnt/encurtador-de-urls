package dev.matheushnt.url_shortener.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.matheushnt.url_shortener.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class TokenProvider {

    private static final String ISSUER = "url_shortener";
    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${app.jwtSecretKey}")
    private String secretkey;

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getId().toString())
                .withIssuer(ISSUER)
                .withIssuedAt(Instant.now())
                .withExpiresAt(this.generateAccessExpirationDate())
                .sign(this.getAlgorithm());
    }

    public Optional<String> validateToken(String token) {
        try {
            DecodedJWT decoded = JWT.require(this.getAlgorithm())
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
            return Optional.of(decoded.getSubject());
        } catch (JWTVerificationException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private Instant generateAccessExpirationDate() {
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretkey);
    }

}
