package com.example.api_oauth2_rbac.dto.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    private String name;
    private String password;

    public String getName() {
        return name.trim();
    }

    public String getPassword() {
        return password.trim();
    }
}
