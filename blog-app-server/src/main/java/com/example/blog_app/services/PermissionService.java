package com.example.blog_app.services;

import com.example.blog_app.models.dtos.PermissionDto;

import java.util.List;

/**
 * Service interface for managing permission-related operations.
 *
 * <p>This interface defines the business logic for permission management,
 * including CRUD operations.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * PermissionService permissionService = new PermissionServiceImpl();
 * PermissionDto permission = permissionService.createPermission(new PermissionDto("READ_PRIVILEGES"));
 * }
 * </pre>
 */
public interface PermissionService {

    /**
     * Creates a new permission.
     *
     * @param permissionDto the DTO containing permission details for creation
     * @return the created permission's details as a response DTO
     */
    PermissionDto createPermission(PermissionDto permissionDto);

    /**
     * Updates an existing permission's details.
     *
     * @param permissionDto   the DTO containing updated permission details
     * @param permissionName  the name of the permission to be updated
     * @return the updated permission's details as a response DTO
     */
    PermissionDto updatePermission(PermissionDto permissionDto, String permissionName);

    /**
     * Retrieves a list of all permissions.
     *
     * @return a list of permission response DTOs representing all permissions
     */
    List<PermissionDto> getAllPermissions();

    /**
     * Deletes a permission by its name.
     *
     * @param permissionName the name of the permission to delete
     */
    void deletePermission(String permissionName);
}
