package com.example.api_oauth2_rbac.repository;

import com.example.api_oauth2_rbac.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsRoleByName(String name);
    Optional<Role> findRoleByName(String name);
    List<Role> findAllByPermissions_Code(String permissionCode);
}
