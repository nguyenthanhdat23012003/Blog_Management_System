package com.example.blog_app.controllers;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.UpdateValidationGroup;
import com.example.blog_app.models.dtos.PermissionRequestDto;
import com.example.blog_app.models.dtos.RoleRequestDto;
import com.example.blog_app.models.dtos.RoleResponseDto;
import com.example.blog_app.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing roles in the application.
 *
 * <p>This controller provides endpoints for creating, retrieving,
 * updating, and deleting roles, as well as managing permissions assigned to roles.</p>
 *
 * <p>Example endpoints:</p>
 * <ul>
 *   <li>POST /api/roles - Create a new role</li>
 *   <li>GET /api/roles - Retrieve all roles</li>
 *   <li>PUT /api/roles/{roleName} - Update an existing role</li>
 *   <li>DELETE /api/roles/{roleName} - Delete a role</li>
 *   <li>GET /api/roles/{roleName}/permissions - Retrieve permissions for a specific role</li>
 *   <li>POST /api/roles/{roleName}/permissions/{permissionName} - Assign a permission to a role</li>
 *   <li>DELETE /api/roles/{roleName}/permissions/{permissionName} - Unassign a permission from a role</li>
 * </ul>
 *
 * @see RoleService
 * @see RoleResponseDto
 * @see PermissionRequestDto
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    /**
     * Constructs a new {@link RoleController} with the given {@link RoleService}.
     *
     * @param roleService the service to handle role-related business logic
     */
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Creates a new role.
     *
     * @param roleDto the DTO containing role details
     * @return the created role as a {@link RoleResponseDto}
     */
    @PostMapping
    public ResponseEntity<RoleResponseDto> createRole(
            @Validated({CreateValidationGroup.class}) @RequestBody RoleRequestDto roleDto) {
        RoleResponseDto createdRole = roleService.createRole(roleDto);
        return ResponseEntity.status(201).body(createdRole);
    }

    /**
     * Retrieves a role by its ID
     *
     * @param roleId the id of the role to update
     * @return the updated role as a {@link RoleResponseDto}
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Long roleId) {
        RoleResponseDto role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    /**
     * Retrieves all roles.
     *
     * @return a list of all roles as {@link RoleResponseDto}
     */
    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<RoleResponseDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Updates an existing role.
     *
     * @param roleDto  the DTO containing updated role details
     * @param roleId the id of the role to update
     * @return the updated role as a {@link RoleResponseDto}
     */
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponseDto> updateRoleById(
            @Validated(UpdateValidationGroup.class) @RequestBody RoleRequestDto roleDto, @PathVariable Long roleId) {
        RoleResponseDto updatedRole = roleService.updateRoleById(roleDto, roleId);
        return ResponseEntity.ok(updatedRole);
    }

    /**
     * Deletes a role.
     *
     * @param roleId the id of the role to delete
     * @return a confirmation message indicating the deletion
     */
    @DeleteMapping("/{roleId}")
    public ResponseEntity<String> deleteRoleById(@PathVariable Long roleId) {
        roleService.deleteRoleById(roleId);
        return ResponseEntity.ok("Role deleted successfully");
    }
}
