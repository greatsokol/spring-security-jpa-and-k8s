package org.greatsokol.springsecurityjpa.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtCore {
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.lifetime}")
    private int jwtLifetime;


    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes()); //Base64.getEncoder().encode(
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userDetails.getLogin())
                .claim("authorities", userDetails.getAuthorities())
                .claim("id", userDetails.getId())
                .claim("name", userDetails.getName())
                .claim("email", userDetails.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtLifetime))
                .signWith(secretKey())
                .compact();
    }

    public String getNameFromJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }
}
