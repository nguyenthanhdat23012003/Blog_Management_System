package com.example.blog_app.controllers;

import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.services.RoleService;
import org.springframework.http.ResponseEntity;
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
 * @see RoleDto
 * @see PermissionDto
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
     * @return the created role as a {@link RoleDto}
     */
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleDto createdRole = roleService.createRole(roleDto);
        return ResponseEntity.status(201).body(createdRole);
    }

    /**
     * Retrieves all roles.
     *
     * @return a list of all roles as {@link RoleDto}
     */
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.status(200).body(roles);
    }

    /**
     * Updates an existing role.
     *
     * @param roleDto  the DTO containing updated role details
     * @param roleName the name of the role to update
     * @return the updated role as a {@link RoleDto}
     */
    @PutMapping("/{roleName}")
    public ResponseEntity<RoleDto> updateRole(@RequestBody RoleDto roleDto, @PathVariable String roleName) {
        RoleDto updatedRole = roleService.updateRole(roleDto, roleName);
        return ResponseEntity.status(200).body(updatedRole);
    }

    /**
     * Deletes a role.
     *
     * @param roleName the name of the role to delete
     * @return a confirmation message indicating the deletion
     */
    @DeleteMapping("/{roleName}")
    public ResponseEntity<String> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ResponseEntity.ok("Role deleted successfully");
    }

    /**
     * Retrieves all permissions assigned to a specific role.
     *
     * @param roleName the name of the role
     * @return a list of permissions assigned to the role as {@link PermissionDto}
     */
    @GetMapping("/{roleName}/permissions")
    public ResponseEntity<List<PermissionDto>> getUserRoles(@PathVariable String roleName) {
        List<PermissionDto> permissions = roleService.getRolePermissions(roleName);
        return ResponseEntity.status(200).body(permissions);
    }

    /**
     * Assigns a permission to a specific role.
     *
     * @param roleName       the name of the role
     * @param permissionName the name of the permission to assign
     * @return a confirmation message indicating the assignment
     */
    @PostMapping("/{roleName}/permissions/{permissionName}")
    public ResponseEntity<String> assignRoleToUser(@PathVariable String roleName, @PathVariable String permissionName) {
        roleService.assignPermissionToRole(roleName, permissionName);
        return ResponseEntity.ok("Assigned permission to role successfully");
    }

    /**
     * Unassigns a permission from a specific role.
     *
     * @param roleName       the name of the role
     * @param permissionName the name of the permission to unassign
     * @return a confirmation message indicating the unassignment
     */
    @DeleteMapping("/{roleName}/permissions/{permissionName}")
    public ResponseEntity<String> unassignRoleToUser(@PathVariable String roleName, @PathVariable String permissionName) {
        roleService.unassignPermissionToRole(roleName, permissionName);
        return ResponseEntity.ok("Unassigned permission from role successfully");
    }
}
