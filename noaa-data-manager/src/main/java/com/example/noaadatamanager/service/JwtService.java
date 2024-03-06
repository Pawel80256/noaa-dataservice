package com.example.noaadatamanager.service;


import com.example.noaadatamanager.models.Role;
import com.example.noaadatamanager.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;


@Service
public class JwtService {
//    private final SecretKey key = Jwts.SIG.HS256.key().build();

    private static final String SECRET_KEY = "mojBardzoTajnyKluczDoGenerowaniaTokenowJWT...";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String generateToken(User user) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600000; // 60 minutes

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRoles().stream().map(Role::getId).collect(Collectors.toList()))
                .issuedAt(new Date(nowMillis))
                .expiration(new Date(expMillis))
                .signWith(key)
                .compact();
    }
}
