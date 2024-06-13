package com.sunbase.customercrud.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Provider for generating and validating JWT tokens.
 */
@Component
public class JwtTokenProvider {

    // Key used to sign the JWT token
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Expiration time for the JWT token (1 week)
    private final long JWT_EXPIRATION = 604800000L;

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param authentication the authentication object containing user details.
     * @return the generated JWT token.
     */
    public String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Retrieves the username from the JWT token.
     *
     * @param token the JWT token.
     * @return the username extracted from the token.
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Validates the JWT token.
     *
     * @param authToken the JWT token.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
