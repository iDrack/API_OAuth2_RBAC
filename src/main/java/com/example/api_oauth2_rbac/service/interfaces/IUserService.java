package com.example.api_oauth2_rbac.service.interfaces;

import com.example.api_oauth2_rbac.dto.user.UserCreate;
import com.example.api_oauth2_rbac.dto.user.UserUpdate;
import com.example.api_oauth2_rbac.model.User;

public interface IUserService {
    public User create(UserCreate userCreateDto) throws IllegalArgumentException;

    public User getByName(String name);

    public User setAdmin(User user);

    public boolean deleteByName(String name);

    User update(UserUpdate userUpdateDto) throws IllegalArgumentException;
}
