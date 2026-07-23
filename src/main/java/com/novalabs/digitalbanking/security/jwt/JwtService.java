package com.novalabs.digitalbanking.security.jwt;

import com.novalabs.digitalbanking.identity.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties properties;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
    }

    /**
     * Generates JWT Access Token
     */
    public String generateToken(UserPrincipal principal) {
        Date now = new Date();

        Date expiry = new Date(now.getTime() + properties.getExpiration());
        return Jwts.builder()
                .subject(principal.getUsername())
                .issuer(properties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validates token signature and expiration.
     */
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException |
                 UnsupportedJwtException |
                 SecurityException |
                 IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Extract username(subject)
     */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extract issuer.
     */
    public String extractIssuer(String token) {
        return parseClaims(token).getIssuer();
    }

    /**
     * Extract expiration.
     */
    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    /**
     * Extract all claims.
     */
    public Claims extractAllClaims(String token) {
        return parseClaims(token);
    }

    /**
     * Returns true if token has expired.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }

    /**
     * Parses JWT and validates signature.
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
