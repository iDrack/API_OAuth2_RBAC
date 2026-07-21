package com.example.api_oauth2_rbac.exception;

import com.example.api_oauth2_rbac.model.Permission;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

public class InsufficientPermissionException extends HttpClientErrorException {
    public InsufficientPermissionException(Permission permissionRequired) {
        super(HttpStatusCode.valueOf(403), "You lack the required permission : " + permissionRequired);
    }
}
