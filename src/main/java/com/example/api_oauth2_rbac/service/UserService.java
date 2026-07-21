package com.example.api_oauth2_rbac.service;

import com.example.api_oauth2_rbac.dto.user.UserCreate;
import com.example.api_oauth2_rbac.dto.user.UserLogin;
import com.example.api_oauth2_rbac.dto.user.UserUpdate;
import com.example.api_oauth2_rbac.model.Role;
import com.example.api_oauth2_rbac.model.User;
import com.example.api_oauth2_rbac.repository.RoleRepository;
import com.example.api_oauth2_rbac.repository.UserRepository;
import com.example.api_oauth2_rbac.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User create(UserCreate userCreateDto) throws IllegalArgumentException {
        if(userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsUserByUsername(userCreateDto.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }
        User newUser = User.builder()
                .username(userCreateDto.getUsername())
                .email(userCreateDto.getEmail())
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .active(true)
                .build();
        newUser.setRoles(roleRepository.findRoleByName("ROLE_USER").map(Set::of).orElseThrow(() -> new IllegalArgumentException("Default role not found")));

        if(userCreateDto.getFirstName() != null) {
            newUser.setFirstName(userCreateDto.getFirstName());
        }
        if(userCreateDto.getLastName() != null) {
            newUser.setLastName(userCreateDto.getLastName());
        }

        newUser = userRepository.save(newUser);
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User login(UserLogin userLoginDto) throws IllegalArgumentException {
        String name = userLoginDto.getUsername();
        if(name.isEmpty() || userLoginDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Name and password are required");
        }
        User user = getByUsername(name);
        if (user == null || !passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> userFound = userRepository.findUserByUsername(username);
        return userFound.orElse(null);
    }

    @Override
    public User setAdmin(UserUpdate userUpdateDto) {
        if (userUpdateDto.getUsername() == null) {
            throw new IllegalArgumentException("User name is required");
        }
        User user = getByUsername(userUpdateDto.getUsername());
        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.setRoles(roleRepository.findRoleByName("ROLE_ADMIN").map(Set::of).orElseThrow(() -> new IllegalArgumentException("Admin role not found")));
        return userRepository.save(user);
    }

    @Override
    public User setRole(UserUpdate userUpdateDto, Set<Role> roles) {
        if (userUpdateDto.getUsername() == null) {
            throw new IllegalArgumentException("User name is required");
        }
        User user = getByUsername(userUpdateDto.getUsername());
        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User addRole(UserUpdate userUpdateDto, Role role) {
        if (userUpdateDto.getUsername() == null) {
            throw new IllegalArgumentException("User name is required");
        }
        User user = getByUsername(userUpdateDto.getUsername());
        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public boolean deleteByUsername(String username) {
        User user = getByUsername(username);
        if(user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Override
    public User update(UserUpdate userUpdateDto) throws IllegalArgumentException {
        if (userUpdateDto.getUsername() == null) {
            throw new IllegalArgumentException("User name is required");
        }
        User user = getByUsername(userUpdateDto.getUsername());
        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (passwordEncoder.matches(userUpdateDto.getPassword(), user.getPassword())) {
            if(userUpdateDto.getFirstName() != null) {
                user.setFirstName(userUpdateDto.getFirstName());
            }
            if(userUpdateDto.getLastName() != null) {
                user.setLastName(userUpdateDto.getLastName());
            }
            if(userUpdateDto.getEmail() != null) {
                user.setEmail(userUpdateDto.getEmail());
            }
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User disableAccount(String username) {
        User user = getByUsername(username);
        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.setActive(false);
        return userRepository.save(user);
    }
}
