package com.example.api_oauth2_rbac.security;

import com.example.api_oauth2_rbac.model.Role;
import com.example.api_oauth2_rbac.model.User;
import com.example.api_oauth2_rbac.repository.RoleRepository;
import com.example.api_oauth2_rbac.service.interfaces.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class JwtService {

    //TODO: Add permissions to jwt + extract
    //TODO: Add  buildAuthentication(String token) =>   transforme le JWT en Authentication Spring Security
    //TODO: Add  getAuthorities(String token) =>  mappe rôles + permissions vers GrantedAuthority
    @Autowired
    private IUserService userService;

    @Value("${JWT_SECRET}")
    private String jwtSecret;
    @Value("${JWT_REFRESH_SECRET}")
    private String jwtRefreshSecret;

    private SecretKey getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        long expirationTime = 60 * 60 * 1000; // 1 hour in milliseconds
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(user.getName())
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .audience().add("access").and()
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(jwtSecret), Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(User user) {
        long expirationTime = 15 * 24 * 60 * 60 * 1000; // 15 jours
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(user.getName())
                .audience().add("refresh").and()
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(jwtRefreshSecret), Jwts.SIG.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    public User extractUser(String token) throws IllegalArgumentException {
        return userService.getByName(extractUsername(token));
    }

    public boolean isValidAccessToken(String token, User user) {
        try {
            String audience = Jwts.parser()
                    .verifyWith(getSigningKey(jwtSecret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getAudience().stream().findFirst().orElse(null);

            String username = extractUsername(token);
            return Objects.equals(audience, "access") && Objects.equals(username, user.getName());
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isValidRefreshToken(String token, User user) {
        try {
            String audience = Jwts.parser()
                    .verifyWith(getSigningKey(jwtRefreshSecret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getAudience().stream().findFirst().orElse(null);

            return Objects.equals(audience, "refresh");
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(getSigningKey(jwtSecret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true; // If the token is invalid, consider it expired
        }
    }
}
