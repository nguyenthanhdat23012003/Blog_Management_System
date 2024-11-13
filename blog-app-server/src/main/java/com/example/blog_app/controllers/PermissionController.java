package com.example.blog_app.controllers;

import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.services.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<PermissionDto> createPermission(@RequestBody PermissionDto permissionDto) {
        PermissionDto createdPermission = permissionService.createPermission(permissionDto);
        return ResponseEntity.status(201).body(createdPermission);
    }

    @GetMapping
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.getAllPermissions();
        return ResponseEntity.status(200).body(permissions);
    }

    @PutMapping("/{permissionName}")
    public ResponseEntity<PermissionDto> updatePermission(@RequestBody PermissionDto permissionDto, @PathVariable String permissionName) {
        PermissionDto updatedPermission = permissionService.updatePermission(permissionDto, permissionName);
        return ResponseEntity.status(200).body(updatedPermission);
    }

    @DeleteMapping("/{permissionName}")
    public ResponseEntity<String> deletePermission(@PathVariable String permissionName) {
        permissionService.deletePermission(permissionName);
        return ResponseEntity.ok("Permission deleted successfully");
    }
}
