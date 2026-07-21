package com.example.api_oauth2_rbac.service.interfaces;

import com.example.api_oauth2_rbac.exception.InsufficientPermissionException;
import com.example.api_oauth2_rbac.model.Permission;


public interface IAccessControlService {
    public boolean hasPermission(String username, Permission permission) throws InsufficientPermissionException;

}
