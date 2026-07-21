package com.example.api_oauth2_rbac.security.aspect;

import com.example.api_oauth2_rbac.exception.InsufficientPermissionException;
import com.example.api_oauth2_rbac.model.Permission;
import com.example.api_oauth2_rbac.security.annotation.RequirePermission;
import com.example.api_oauth2_rbac.service.interfaces.IAccessControlService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class PermissionAspect {
    private final IAccessControlService accessControlService;

    public PermissionAspect(IAccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("User is not authenticated.");
        }
        String username = authentication.getName();
        Permission permission = requirePermission.value();
        boolean allowed = accessControlService.hasPermission(username, permission);
        if (!allowed) {
            throw new InsufficientPermissionException(permission);
        }
        return joinPoint.proceed();
    }
}
