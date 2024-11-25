package com.example.blog_app.services;

import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.dtos.RoleDto;

import java.util.List;

/**
 * Service interface for managing role-related operations.
 *
 * <p>This interface defines the business logic for role management,
 * including CRUD operations and permission assignments.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * RoleService roleService = new RoleServiceImpl();
 * RoleDto role = roleService.createRole(new RoleDto("ADMIN"));
 * }
 * </pre>
 */
public interface RoleService {

    /**
     * Creates a new role.
     *
     * @param roleDto the DTO containing role details for creation
     * @return the created role's details as a response DTO
     */
    RoleDto createRole(RoleDto roleDto);

    /**
     * Updates an existing role's details.
     *
     * @param roleDto  the DTO containing updated role details
     * @param roleName the name of the role to be updated
     * @return the updated role's details as a response DTO
     */
    RoleDto updateRole(RoleDto roleDto, String roleName);

    /**
     * Retrieves a list of all roles.
     *
     * @return a list of role response DTOs representing all roles
     */
    List<RoleDto> getAllRoles();

    /**
     * Deletes a role by its name.
     *
     * @param roleName the name of the role to delete
     */
    void deleteRole(String roleName);

    /**
     * Retrieves the permissions assigned to a specific role.
     *
     * @param roleName the name of the role
     * @return a list of permission DTOs assigned to the role
     */
    List<PermissionDto> getRolePermissions(String roleName);

    /**
     * Assigns a permission to a role.
     *
     * @param roleName       the name of the role
     * @param permissionName the name of the permission to assign
     */
    void assignPermissionToRole(String roleName, String permissionName);

    /**
     * Unassigns a permission from a role.
     *
     * @param roleName       the name of the role
     * @param permissionName the name of the permission to unassign
     */
    void unassignPermissionToRole(String roleName, String permissionName);
}
