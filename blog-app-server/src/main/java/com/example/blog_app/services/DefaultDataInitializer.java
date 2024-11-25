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

/**
 * Service responsible for initializing default data in the application.
 *
 * <p>This class sets up the default roles, permissions, and admin user
 * when the application starts. It ensures that the necessary data
 * exists for the application to function correctly.</p>
 *
 * <p>Features:</p>
 * <ul>
 *     <li>Creates default permissions if they don't exist.</li>
 *     <li>Creates default roles (ADMIN, USER) if they don't exist.</li>
 *     <li>Assigns permissions to roles.</li>
 *     <li>Creates a default admin user with predefined properties.</li>
 * </ul>
 *
 * <p>This class uses {@link PostConstruct} to execute initialization logic
 * after the application context is fully set up.</p>
 */
@Service
public class DefaultDataInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    /**
     * Constructs the DefaultDataInitializer with required dependencies.
     *
     * @param roleRepository      the repository for managing roles
     * @param permissionRepository the repository for managing permissions
     * @param userRepository       the repository for managing users
     * @param passwordEncoder      the encoder for hashing passwords
     * @param adminProperties      the configuration properties for the default admin user
     */
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

    /**
     * Initializes default data for the application.
     *
     * <p>Executed automatically after the application starts. This method ensures
     * that default permissions, roles, and the admin user are created.</p>
     */
    @PostConstruct
    public void initializeDefaultData() {
        createDefaultPermissions();
        Role adminRole = createRoleIfNotExists("ADMIN");
        Role userRole = createRoleIfNotExists("USER");

        assignPermissionsToRole(adminRole, List.of(
                "VIEW_USER", "CREATE_USER", "UPDATE_USER", "DELETE_USER",
                "VIEW_BLOG", "CREATE_BLOG", "UPDATE_BLOG", "DELETE_BLOG",
                "VIEW_CATEGORY", "CREATE_CATEGORY", "UPDATE_CATEGORY", "DELETE_CATEGORY",
                "VIEW_SERIES", "CREATE_SERIES", "UPDATE_SERIES", "DELETE_SERIES"
        ));

        assignPermissionsToRole(userRole, List.of(
                "VIEW_BLOG", "CREATE_BLOG", "UPDATE_BLOG", "DELETE_BLOG",
                "VIEW_CATEGORY", "CREATE_CATEGORY", "UPDATE_CATEGORY", "DELETE_CATEGORY",
                "VIEW_SERIES", "CREATE_SERIES", "UPDATE_SERIES", "DELETE_SERIES"
        ));

        createDefaultAdminUser(adminRole);
    }

    /**
     * Creates default permissions if they do not exist in the database.
     */
    private void createDefaultPermissions() {
        List<String> permissions = List.of(
                "VIEW_USER", "CREATE_USER", "UPDATE_USER", "DELETE_USER",
                "VIEW_BLOG", "CREATE_BLOG", "UPDATE_BLOG", "DELETE_BLOG",
                "VIEW_CATEGORY", "CREATE_CATEGORY", "UPDATE_CATEGORY", "DELETE_CATEGORY",
                "VIEW_SERIES", "CREATE_SERIES", "UPDATE_SERIES", "DELETE_SERIES"
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

    /**
     * Creates a role if it does not already exist.
     *
     * @param roleName the name of the role to create
     * @return the existing or newly created role
     */
    private Role createRoleIfNotExists(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            role.setImmutable(Boolean.TRUE);
            return roleRepository.save(role);
        });
    }

    /**
     * Assigns a list of permissions to a role.
     *
     * @param role            the role to which permissions will be assigned
     * @param permissionNames the list of permission names to assign
     */
    private void assignPermissionsToRole(Role role, List<String> permissionNames) {
        role.setPermissions(new HashSet<>());
        for (String permissionName : permissionNames) {
            Permission permission = permissionRepository.findByName(permissionName)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permissionName));
            role.getPermissions().add(permission);
        }
        roleRepository.save(role);
    }

    /**
     * Creates the default admin user with the ADMIN role.
     *
     * @param adminRole the ADMIN role to assign to the user
     */
    private void createDefaultAdminUser(Role adminRole) {
        if (userRepository.findByEmail(adminProperties.getEmail()).isEmpty()) {
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
