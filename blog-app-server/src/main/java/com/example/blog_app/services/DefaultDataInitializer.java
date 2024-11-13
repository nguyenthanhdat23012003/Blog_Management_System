package com.example.blog_app.services;

import com.example.blog_app.config.AdminProperties;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.repositories.PermissionRepository;
import com.example.blog_app.repositories.RoleRepository;
import com.example.blog_app.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class DefaultDataInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    public DefaultDataInitializer(RoleRepository roleRepository,
                                  PermissionRepository permissionRepository,
                                  UserRepository userRepository,
                                  BCryptPasswordEncoder passwordEncoder,
                                  AdminProperties adminProperties) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
    }

    @PostConstruct
    public void initializeDefaultData() {
        createDefaultPermissions();
        Role adminRole = createRoleIfNotExists("ADMIN");
        Role userRole = createRoleIfNotExists("USER");

        assignPermissionsToRole(adminRole, List.of(
                "VIEW_USER", "CREATE_USER", "UPDATE_USER", "DELETE_USER",
                "VIEW_BLOG", "CREATE_BLOG", "UPDATE_BLOG", "DELETE_BLOG",
                "VIEW_CATEGORY", "CREATE_CATEGORY", "UPDATE_CATEGORY", "DELETE_CATEGORY"
        ));

        assignPermissionsToRole(userRole, List.of(
                "VIEW_BLOG", "CREATE_BLOG", "UPDATE_BLOG", "DELETE_BLOG",
                "VIEW_CATEGORY", "CREATE_CATEGORY", "UPDATE_CATEGORY", "DELETE_CATEGORY"
        ));

        createDefaultAdminUser(adminRole);
    }

    private void createDefaultPermissions() {
        List<String> permissions = List.of(
                "VIEW_USER", "CREATE_USER", "UPDATE_USER", "DELETE_USER",
                "VIEW_BLOG", "CREATE_BLOG", "UPDATE_BLOG", "DELETE_BLOG",
                "VIEW_CATEGORY", "CREATE_CATEGORY", "UPDATE_CATEGORY", "DELETE_CATEGORY"
        );

        for (String permissionName : permissions) {
            if (permissionRepository.findByName(permissionName).isEmpty()) {
                Permission permission = new Permission();
                permission.setName(permissionName);
                permission.setImmutable(Boolean.TRUE);
                permissionRepository.save(permission);
            }
        }
    }

    private Role createRoleIfNotExists(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            role.setImmutable(Boolean.TRUE);
            return roleRepository.save(role);
        });
    }

    private void assignPermissionsToRole(Role role, List<String> permissionNames) {
        role.setPermissions(new HashSet<>());
        for (String permissionName : permissionNames) {
            Permission permission = permissionRepository.findByName(permissionName)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permissionName));
            role.getPermissions().add(permission);
        }
        roleRepository.save(role);
    }

    private void createDefaultAdminUser(Role adminRole) {
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setName(this.adminProperties.getName());
            admin.setEmail(this.adminProperties.getEmail());
            admin.setPassword(passwordEncoder.encode(this.adminProperties.getPassword())); // Hash password
            admin.setAbout(this.adminProperties.getAbout());
            admin.setImmutable(Boolean.TRUE);
            admin.getRoles().add(adminRole);
            userRepository.save(admin);
        }
    }
}

