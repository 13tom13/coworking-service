package io.ylab.tom13.coworkingservice.out.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey SECRET_KEY;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;
    private static final String SECRET_IS_NOT_SET = "JWT секрет не задан";
    private static final String ID = "id";
    private static final String ROLE = "role";

    @PostConstruct
    public void init() {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException(SECRET_IS_NOT_SET);
        }
        SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateJwt(long id, String role) {
        return Jwts.builder()
                .setClaims(Map.of(ID, id, ROLE, role))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .setSubject(String.valueOf(id))
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validJwt(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.get(ID).toString());
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(ROLE).toString();
    }
}