package ru.inovus.policeman.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.inovus.policeman.model.User;
import ru.inovus.policeman.security.jwt.exception.JwtParsingException;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey jwtSecret;

    public JwtProvider(
            @Value("${jwt.secret}") String jwtSecret
    ) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant expirationInstant = now.plusDays(7).atZone(ZoneId.systemDefault()).toInstant();
        final Date expiration = Date.from(expirationInstant);
        return Jwts.builder()
                .setSubject(user.getId())
                .setExpiration(expiration)
                .signWith(jwtSecret)
                .claim("name", user.getName())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (RuntimeException e) {
            throw new JwtParsingException(e.getMessage());
        }
    }

    public Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
