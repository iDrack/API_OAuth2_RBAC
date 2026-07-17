package com.example.api_oauth2_rbac.controller;

import com.example.api_oauth2_rbac.dto.user.UserCreate;
import com.example.api_oauth2_rbac.dto.user.UserLogin;
import com.example.api_oauth2_rbac.security.service.JwtService;
import com.example.api_oauth2_rbac.service.interfaces.IUserService;
import com.example.api_oauth2_rbac.utils.DtoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private IUserService userService;
    @Autowired
    private JwtService jwtService;

    private final Pattern passwordRegex = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
    private final Pattern emailRegex = Pattern.compile("^(.+)@(\\S+)$");

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLogin userLoginDto) {
        var user = userService.login(userLoginDto);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return ResponseEntity.ok(Map.of("access_token", accessToken, "refresh_token", refreshToken));
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserCreate userCreateDto) {
        try {
            Matcher passMatcher = userCreateDto.getPassword() != null ? passwordRegex.matcher(userCreateDto.getPassword()) : null;
            if (passMatcher == null || !passMatcher.matches()) {
                return ResponseEntity.status(400).body(Map.of("error", "Password does not meet the required criteria"));
            }
            Matcher mailMatcher = userCreateDto.getEmail() != null ? emailRegex.matcher(userCreateDto.getEmail()) : null;
            if (mailMatcher == null || !mailMatcher.matches()) {
                return ResponseEntity.status(400).body(Map.of("error", "Email is invalid"));
            }
            userService.create(userCreateDto);
            UserLogin newUser = new UserLogin(userCreateDto.getUsername(), userCreateDto.getPassword());
            return login(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}
