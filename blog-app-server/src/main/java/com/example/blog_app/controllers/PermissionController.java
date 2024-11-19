package com.example.blog_app.controllers;

import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.services.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing permissions in the application.
 *
 * <p>This controller provides endpoints for creating, retrieving,
 * updating, and deleting permissions. It uses {@link PermissionService}
 * to handle the business logic and interacts with DTOs for request and response.</p>
 *
 * <p>Example endpoints:</p>
 * <ul>
 *   <li>POST /api/permissions - Create a new permission</li>
 *   <li>GET /api/permissions - Retrieve all permissions</li>
 *   <li>PUT /api/permissions/{permissionName} - Update an existing permission</li>
 *   <li>DELETE /api/permissions/{permissionName} - Delete a permission</li>
 * </ul>
 *
 * @see PermissionService
 * @see PermissionDto
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Constructs a new {@link PermissionController} with the given {@link PermissionService}.
     *
     * @param permissionService the service to handle permission-related business logic
     */
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * Creates a new permission.
     *
     * @param permissionDto the DTO containing permission details
     * @return the created permission as a {@link PermissionDto}
     */
    @PostMapping
    public ResponseEntity<PermissionDto> createPermission(@RequestBody PermissionDto permissionDto) {
        PermissionDto createdPermission = permissionService.createPermission(permissionDto);
        return ResponseEntity.status(201).body(createdPermission);
    }

    /**
     * Retrieves all permissions.
     *
     * @return a list of all permissions as {@link PermissionDto}
     */
    @GetMapping
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.getAllPermissions();
        return ResponseEntity.status(200).body(permissions);
    }

    /**
     * Updates an existing permission.
     *
     * @param permissionDto  the DTO containing updated permission details
     * @param permissionName the name of the permission to update
     * @return the updated permission as a {@link PermissionDto}
     */
    @PutMapping("/{permissionName}")
    public ResponseEntity<PermissionDto> updatePermission(
            @RequestBody PermissionDto permissionDto,
            @PathVariable String permissionName) {
        PermissionDto updatedPermission = permissionService.updatePermission(permissionDto, permissionName);
        return ResponseEntity.status(200).body(updatedPermission);
    }

    /**
     * Deletes a permission.
     *
     * @param permissionName the name of the permission to delete
     * @return a confirmation message indicating the deletion
     */
    @DeleteMapping("/{permissionName}")
    public ResponseEntity<String> deletePermission(@PathVariable String permissionName) {
        permissionService.deletePermission(permissionName);
        return ResponseEntity.ok("Permission deleted successfully");
    }
}
