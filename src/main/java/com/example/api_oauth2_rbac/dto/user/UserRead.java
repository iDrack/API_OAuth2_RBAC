package com.example.api_oauth2_rbac.dto.user;

import com.example.api_oauth2_rbac.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserRead {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    @Getter
    private Set<Role> roles;

    public String getUsername() {
        return username.trim();
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

}
