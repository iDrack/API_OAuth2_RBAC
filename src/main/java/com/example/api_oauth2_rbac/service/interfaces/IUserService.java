package com.example.api_oauth2_rbac.service.interfaces;

import com.example.api_oauth2_rbac.dto.user.UserCreate;
import com.example.api_oauth2_rbac.dto.user.UserLogin;
import com.example.api_oauth2_rbac.dto.user.UserUpdate;
import com.example.api_oauth2_rbac.model.Role;
import com.example.api_oauth2_rbac.model.User;

import java.util.Set;

public interface IUserService {
    public User create(UserCreate userCreateDto) throws IllegalArgumentException;

    public User login(UserLogin userLoginDto)throws IllegalArgumentException;

    public User getByName(String name);

    public User setAdmin(UserUpdate userUpdateDto);

    public User setRole(UserUpdate userUpdateDto, Set<Role> roles);

    public User addRole(UserUpdate userUpdateDto, Role role);

    public boolean deleteByName(String name);

    User update(UserUpdate userUpdateDto) throws IllegalArgumentException;

    User disableAccount(String name);
}
