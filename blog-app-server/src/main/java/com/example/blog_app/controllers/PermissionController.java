package com.example.blog_app.controllers;

import com.example.blog_app.models.dtos.PermissionRequestDto;
import com.example.blog_app.models.dtos.PermissionResponseDto;
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
 *   <li>GET /api/permissions/{permissionId} - Retrieve a permission by ID</li>
 *   <li>PUT /api/permissions/{permissionId} - Update an existing permission</li>
 *   <li>DELETE /api/permissions/{permissionId} - Delete a permission</li>
 * </ul>
 *
 * @see PermissionService
 * @see PermissionRequestDto
 * @see PermissionResponseDto
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
     * @return the created permission as a {@link PermissionResponseDto}
     */
    @PostMapping
    public ResponseEntity<PermissionResponseDto> createPermission(@RequestBody PermissionRequestDto permissionDto) {
        PermissionResponseDto createdPermission = permissionService.createPermission(permissionDto);
        return ResponseEntity.status(201).body(createdPermission);
    }

    /**
     * Retrieves all permissions.
     *
     * @return a list of all permissions as {@link PermissionResponseDto}
     */
    @GetMapping
    public ResponseEntity<List<PermissionResponseDto>> getAllPermissions() {
        List<PermissionResponseDto> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    /**
     * Retrieves a permission by its ID.
     *
     * @param permissionId the ID of the permission to retrieve
     * @return the permission as a {@link PermissionResponseDto}
     */
    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionResponseDto> getPermissionById(@PathVariable Long permissionId) {
        PermissionResponseDto permission = permissionService.getPermissionById(permissionId);
        return ResponseEntity.ok(permission);
    }

    /**
     * Updates an existing permission.
     *
     * @param permissionDto the DTO containing updated permission details
     * @param permissionId  the ID of the permission to update
     * @return the updated permission as a {@link PermissionResponseDto}
     */
    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionResponseDto> updatePermissionById(
            @RequestBody PermissionRequestDto permissionDto,
            @PathVariable Long permissionId) {
        PermissionResponseDto updatedPermission = permissionService.updatePermissionById(permissionDto, permissionId);
        return ResponseEntity.ok(updatedPermission);
    }

    /**
     * Deletes a permission.
     *
     * @param permissionId the ID of the permission to delete
     * @return a confirmation message indicating the deletion
     */
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<String> deletePermissionById(@PathVariable Long permissionId) {
        permissionService.deletePermissionById(permissionId);
        return ResponseEntity.ok("Permission deleted successfully");
    }
}
