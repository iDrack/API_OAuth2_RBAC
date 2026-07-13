package com.example.api_oauth2_rbac.init;

import com.example.api_oauth2_rbac.model.Permission;
import com.example.api_oauth2_rbac.model.Role;
import com.example.api_oauth2_rbac.model.User;
import com.example.api_oauth2_rbac.repository.PermissionRepository;
import com.example.api_oauth2_rbac.repository.RoleRepository;
import com.example.api_oauth2_rbac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_PASS}")
    private String adminPass;

    @Override
    public void run(String... args) throws Exception {

        // Créer les permissions
        if (permissionRepository.count() == 0) {
            Permission[] perms = {
                    Permission.builder().code("USER_READ").description("Lire les utilisateurs").build(),
                    Permission.builder().code("USER_CREATE").description("Créer des utilisateurs").build(),
                    Permission.builder().code("USER_DELETE").description("Supprimer des utilisateurs").build(),
                    Permission.builder().code("RESOURCE_READ").description("Lire les ressources").build(),
                    Permission.builder().code("RESOURCE_DELETE").description("Supprimer ses ressources").build(),
                    Permission.builder().code("ADMIN_ACCESS").description("Accès administrateur").build()
            };
            permissionRepository.saveAll(Arrays.asList(perms));
        }

        // Créer les rôles
        if (roleRepository.findRoleByName("ROLE_USER").isEmpty()) {
            Set<Permission> userPerms = permissionRepository.findAllByCategory(null).stream()
                    .filter(p -> !p.getCode().equals("ADMIN_ACCESS")).collect(Collectors.toSet());

            Role userRole = Role.builder()
                    .name("ROLE_USER")
                    .description("Utilisateur standard")
                    .permissions(userPerms)
                    .build();
            roleRepository.save(userRole);
        }

        if (roleRepository.findRoleByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = Role.builder()
                    .name("ROLE_ADMIN")
                    .description("Administrateur")
                    .permissions(new HashSet<>(permissionRepository.findAll()))
                    .build();
            roleRepository.save(adminRole);
        }

        // Créer un utilisateur admin par défaut
        if (userRepository.findUserByName("admin").isEmpty()) {
            User admin = User.builder()
                    .name("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode(adminPass))
                    .firstName("Admin")
                    .lastName("System")
                    .active(true)
                    .roles(Set.of(roleRepository.findRoleByName("ROLE_ADMIN").orElseThrow()))
                    .build();
            userRepository.save(admin);
        }
    }
}
