package com.example.blog_app.controllers;

import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleDto createdRole = roleService.createRole(roleDto);
        return ResponseEntity.status(201).body(createdRole);
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.status(200).body(roles);
    }

    @PutMapping("/{roleName}")
    public ResponseEntity<RoleDto> updateRole(@RequestBody RoleDto roleDto, @PathVariable String roleName) {
        RoleDto updatedRole = roleService.updateRole(roleDto, roleName);
        return ResponseEntity.status(200).body(updatedRole);
    }

    @DeleteMapping("/{roleName}")
    public ResponseEntity<String> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ResponseEntity.ok("Role deleted successfully");
    }

    @GetMapping("/{roleName}/permissions")
    public ResponseEntity<List<PermissionDto>> getUserRoles(@PathVariable String roleName) {
        List<PermissionDto> permissions = roleService.getRolePermissions(roleName);
        return ResponseEntity.status(200).body(permissions);
    }

    @PostMapping("/{roleName}/permissions/{permissionName}")
    public ResponseEntity<String> assignRoleToUser(@PathVariable String roleName, @PathVariable String permissionName) {
        roleService.assignPermissionToRole(roleName, permissionName);
        return ResponseEntity.ok("Assigned permission to role successfully");
    }

    @DeleteMapping("/{roleName}/permissions/{permissionName}")
    public ResponseEntity<String> unassignRoleToUser(@PathVariable String roleName, @PathVariable String permissionName) {
        roleService.unassignPermissionToRole(roleName, permissionName);
        return ResponseEntity.ok("Assigned permission to role successfully");
    }
}
