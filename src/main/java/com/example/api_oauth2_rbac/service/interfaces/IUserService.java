package com.example.api_oauth2_rbac.service.interfaces;

import com.example.api_oauth2_rbac.dto.user.UserCreate;
import com.example.api_oauth2_rbac.dto.user.UserLogin;
import com.example.api_oauth2_rbac.dto.user.UserUpdate;
import com.example.api_oauth2_rbac.model.Role;
import com.example.api_oauth2_rbac.model.User;

import java.util.List;
import java.util.Set;

public interface IUserService {
    public User create(UserCreate userCreateDto) throws IllegalArgumentException;

    public List<User> getAllUsers();

    public User login(UserLogin userLoginDto)throws IllegalArgumentException;

    public User getByUsername(String username);

    public User setAdmin(UserUpdate userUpdateDto);

    public User setRole(UserUpdate userUpdateDto, Set<Role> roles);

    public User addRole(UserUpdate userUpdateDto, Role role);

    public boolean deleteByUsername(String username);

    User update(UserUpdate userUpdateDto) throws IllegalArgumentException;

    User disableAccount(String username);
}
