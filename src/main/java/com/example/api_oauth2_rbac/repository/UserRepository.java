package com.example.api_oauth2_rbac.repository;

import com.example.api_oauth2_rbac.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {
    boolean existsUserByName(String name);

    boolean existsByEmail(String email);

    Optional<User> findUserByName(String name);

    Optional<User> findByEmail(String email);

    Optional<User> findUserById(Long id);
}
