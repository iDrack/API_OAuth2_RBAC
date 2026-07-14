package com.example.api_oauth2_rbac.controller;

import com.example.api_oauth2_rbac.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/test", produces = "application/json")
public class TestController {

    @Autowired
    private JwtService jwtService;

    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        return ResponseEntity.ok(Map.of("data", "This is a public endpoint"));
    }

    @GetMapping("/admin")
    public ResponseEntity<Map<String, String>> adminOnlyEndpoint(
            @RequestHeader(value = "Authorization", required = true) String authToken
    ) {
        if (authToken == null || jwtService.isTokenExpired(authToken)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        var user = jwtService.extractUser(authToken);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        if (!user.isActive()) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        if (!jwtService.isValidAccessToken(authToken, user) || jwtService.extractRoles(authToken)
                .stream()
                .noneMatch(role -> role.equals("ROLE_ADMIN"))
        ) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        return ResponseEntity.ok(Map.of("data", "This is not a public endpoint"));
    }
}
