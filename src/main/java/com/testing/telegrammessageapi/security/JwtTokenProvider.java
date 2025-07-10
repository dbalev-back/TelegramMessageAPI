package com.testing.telegrammessageapi.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.expiration}")
    private long expiration;
    private final SecretKey secret;

    public JwtTokenProvider() {
        this.secret = Keys.secretKeyFor(SignatureAlgorithm.HS256); // сгенерированный безопасный ключ
    }

    public String generateToken(String username) {
        Date nowDate = new Date();
        Date expirationDate = new Date(nowDate.getTime() + expiration);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expirationDate)
                .signWith(secret)
                .compact();
    }

    public String getUsernameByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
