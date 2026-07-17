package com.example.api_oauth2_rbac.service.interfaces;

import com.example.api_oauth2_rbac.model.User;

public interface IAccessControlService {
    public boolean hasPermission(String username, String permissionCode);

}
