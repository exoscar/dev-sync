package org.devsync.spring.common.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtService {
    private final Long expirationMs;
    private final SecretKey secretKey;

    public JwtService(
            @Value("${app.jwt.expiration-ms}") Long expirationMs,
            @Value("${app.jwt.secret}") String secret) {

        this.expirationMs = expirationMs;
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateAccessToken(UUID userId){
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public Claims extractClaims(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public UUID extractUserId(String token){
        return UUID.fromString(
                extractClaims(token).getSubject()
        );
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (ExpiredJwtException ex) {
            log.warn("JWT token has expired: {}", ex.getMessage());

        } catch (MalformedJwtException ex) {
            log.warn("Invalid JWT token format: {}", ex.getMessage());

        } catch (SecurityException ex) {
            log.warn("JWT signature validation failed: {}", ex.getMessage());

        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT token: {}", ex.getMessage());

        } catch (IllegalArgumentException ex) {
            log.warn("JWT token is null or empty");

        } catch (WeakKeyException ex) {
            log.error("JWT secret key is too weak: {}", ex.getMessage());

        } catch (Exception ex) {
            log.error("Unexpected error while validating JWT", ex);
        }

        return false;
    }


}
