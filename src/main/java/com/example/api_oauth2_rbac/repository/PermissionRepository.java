package com.example.api_oauth2_rbac.repository;

import com.example.api_oauth2_rbac.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Boolean existsPermissionByCode(String code);

    Optional<Permission> findPermissionByCode(String code);

    List<Permission> findAllByCategory(Permission.Category category);
}
