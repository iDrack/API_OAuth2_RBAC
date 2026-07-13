package com.example.api_oauth2_rbac.security;

import com.example.api_oauth2_rbac.model.Role;
import com.example.api_oauth2_rbac.model.User;
import com.example.api_oauth2_rbac.service.interfaces.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Service
public class JwtService {

    //TODO: Add permissions to jwt + extract
    //TODO: Add  buildAuthentication(String token) =>   transforme le JWT en Authentication Spring Security
    //TODO: Add  getAuthorities(String token) =>  mappe rôles + permissions vers GrantedAuthority
    @Autowired
    private IUserService userService;

    private final String jwtSecret = System.getenv("JWT_SECRET");
    private final String jwtRefreshSecret = System.getenv("JWT_REFRESH_SECRET");

    public String generateAccessToken(User user) {
        long expirationTime = 60 * 60 * 1000; // 1 hour in milliseconds
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(user.getName())
                .claim("roles", user.getRoles())
                .setAudience("access")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateRefreshToken(User user) {
        long expirationTime = 15 * 24 * 60 * 60 * 1000; // 15 jours
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(user.getName())
                .setAudience("refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtRefreshSecret)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Set<Role> extractRoles(String token) {
        Set<Role> roles = extractClaims(token).get("roles", Set.class);
        return roles;
    }

    public boolean isValidAccessToken(String token, User user) {
        try {
            String audience = Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getAudience();

            String username = extractUsername(token);
            return Objects.equals(audience, "access") && Objects.equals(username, user.getName());
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isValidRefreshToken(String token, User user) {
        try {
            String audience = Jwts.parserBuilder().setSigningKey(jwtRefreshSecret).build().parseClaimsJws(token).getBody().getAudience();

            return Objects.equals(audience, "refresh");
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true; // If the token is invalid, consider it expired
        }
    }
}
