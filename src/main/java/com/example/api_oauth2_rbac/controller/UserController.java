package com.example.api_oauth2_rbac.controller;

import com.example.api_oauth2_rbac.dto.user.UserRead;
import com.example.api_oauth2_rbac.model.User;
import com.example.api_oauth2_rbac.security.annotation.RequirePermission;
import com.example.api_oauth2_rbac.service.interfaces.IUserService;
import com.example.api_oauth2_rbac.utils.DtoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private DtoTools dtoTools;

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("isAuthenticated()")
    @RequirePermission("USER_DELETE")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username) {
        if (userService.deleteByUsername(username)) {
            return ResponseEntity.ok(Map.of(
                    "data", "User " + username + " has been deleted permanently."
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "data", "User not found."
            ));
        }
    }

    @GetMapping(value = "/profile", produces = "application/json")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserRead> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dtoTools.convertToDto(currentUser, UserRead.class));
    }
}
