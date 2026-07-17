package com.example.api_oauth2_rbac.service;

import com.example.api_oauth2_rbac.model.Permission;
import com.example.api_oauth2_rbac.model.Role;
import com.example.api_oauth2_rbac.model.User;
import com.example.api_oauth2_rbac.repository.PermissionRepository;
import com.example.api_oauth2_rbac.service.interfaces.IAccessControlService;
import com.example.api_oauth2_rbac.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AccessControlService implements IAccessControlService {
    @Autowired
    private IUserService userService;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public boolean hasPermission(String username, String permissionCode) {
        if (!permissionRepository.existsPermissionByCode(permissionCode)) {
            throw new RuntimeException("Permission with code : " + permissionCode + " not found.");
        }
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
        return user.getRoles()
                .stream()
                .anyMatch(
                        role -> role.getPermissions()
                                .stream()
                                .anyMatch(
                                        p -> p.getCode().equals(permissionCode)
                                )
                );
    }
}
