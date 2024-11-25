package com.example.blog_app.services;

import com.example.blog_app.models.dtos.PermissionRequestDto;
import com.example.blog_app.models.dtos.PermissionResponseDto;

import java.util.List;

/**
 * Service interface for managing permission-related operations.
 *
 * <p>This interface defines the business logic for permission management,
 * including CRUD operations.</p>
 *
 * @see PermissionRequestDto
 * @see PermissionResponseDto
 */
public interface PermissionService {

    /**
     * Creates a new permission.
     *
     * @param permissionDto the DTO containing permission details for creation
     * @return the created permission's details as a {@link PermissionResponseDto}
     */
    PermissionResponseDto createPermission(PermissionRequestDto permissionDto);

    /**
     * Updates an existing permission's details.
     *
     * @param permissionDto the DTO containing updated permission details
     * @param id the ID of the permission to be updated
     * @return the updated permission's details as a {@link PermissionResponseDto}
     */
    PermissionResponseDto updatePermissionById(PermissionRequestDto permissionDto, Long id);

    /**
     * Retrieves a permission by its ID.
     *
     * @param id the ID of the permission to retrieve
     * @return the permission's details as a {@link PermissionResponseDto}
     * @throws com.example.blog_app.exceptions.ResourceNotFoundException if the permission does not exist
     */
    PermissionResponseDto getPermissionById(Long id);

    /**
     * Retrieves a list of all permissions.
     *
     * @return a list of permission response DTOs representing all permissions
     */
    List<PermissionResponseDto> getAllPermissions();

    /**
     * Deletes a permission by its ID.
     *
     * @param id the ID of the permission to delete
     */
    void deletePermissionById(Long id);
}
