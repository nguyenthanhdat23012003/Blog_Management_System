package com.example.blog_app.services;

import com.example.blog_app.models.dtos.RoleRequestDto;
import com.example.blog_app.models.dtos.RoleResponseDto;

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
    RoleResponseDto createRole(RoleRequestDto roleDto);

    /**
     * Updates an existing role's details.
     *
     * @param roleDto  the DTO containing updated role details
     * @param roleId the id of the role to be updated
     * @return the updated role's details as a response DTO
     */
    RoleResponseDto updateRoleById(RoleRequestDto roleDto, Long roleId);

    /**
     * Retrieves the details of a role by its ID.
     *
     * <p>Fetches the role's information, including its permissions.</p>
     *
     * @param roleId the ID of the role to retrieve
     * @return the role's details as a response DTO
     * @throws com.example.blog_app.exceptions.ResourceNotFoundException if the role does not exist
     */
    RoleResponseDto getRoleById(Long roleId);

    /**
     * Retrieves a list of all roles.
     *
     * @return a list of role response DTOs representing all roles
     */
    List<RoleResponseDto> getAllRoles();

    /**
     * Deletes a role by its name.
     *
     * @param roleId the id of the role to delete
     */
    void deleteRoleById(Long roleId);
}
