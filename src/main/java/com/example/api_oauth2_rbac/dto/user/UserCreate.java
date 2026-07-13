package com.example.api_oauth2_rbac.dto.user;

import lombok.Data;

//TODO: Ajouter les dto de création et de modification des suers et implémenter leurs services
@Data
public class UserCreate {
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
