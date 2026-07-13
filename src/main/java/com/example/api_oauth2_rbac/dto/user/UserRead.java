package com.example.api_oauth2_rbac.dto.user;

import com.example.api_oauth2_rbac.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserRead {
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Role> roles;
}
