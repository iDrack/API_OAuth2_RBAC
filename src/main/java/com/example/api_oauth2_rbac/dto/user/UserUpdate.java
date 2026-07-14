package com.example.api_oauth2_rbac.dto.user;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserUpdate {
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String newPassword;

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
        return email.toLowerCase().trim();
    }

    public String getPassword() {
        return password.trim();
    }

    public String getNewPassword() {
        return newPassword.trim();
    }
}
