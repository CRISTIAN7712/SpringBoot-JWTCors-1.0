package com.example.jwtcors.service;

import com.example.jwtcors.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username, Role role){
        // Convertir permisos a lista de String
        List<String> permissions = role.permissions.stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .claim("permissions", permissions)
                .claim("authorities", role.getAuthorities())
                .setExpiration(new Date(System.currentTimeMillis()+3600_000))
                .signWith(key)
                .compact();
    }

    public List<String> getPermissions(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("permissions", List.class);
    }

    public String getRole(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public List<String> getAuthorities(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("authorities", List.class);
    }

    public String getUsername(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
