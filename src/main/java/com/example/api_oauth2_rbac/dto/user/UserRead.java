package com.example.api_oauth2_rbac.dto.user;

import com.example.api_oauth2_rbac.model.Role;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class UserRead {
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Role> roles;

    public String getName() {
        return name.trim();
    }

    public String getFirstName() {
        return firstName.trim();
    }

    public String getLastName() {
        return lastName.trim();
    }

    public String getEmail() {
        return email.trim();
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
