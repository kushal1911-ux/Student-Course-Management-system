package com.scms.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// Utility class - creates and validates JWT tokens
@Component
public class JwtUtil {

    // Secret key used to sign tokens (auto-generated at startup; in real production,
    // this should be a fixed, securely stored value so tokens survive app restarts)
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token validity: 1 hour (in milliseconds)
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    // Generates a new JWT token containing the username, issued-at, and expiry time
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    // Extracts the username stored inside a given token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Checks if a token's signature is valid and it hasn't expired
    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}