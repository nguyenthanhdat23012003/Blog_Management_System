package com.example.blog_app.services;

import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.models.entities.Role;

import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    RoleDto updateRole(RoleDto roleDto, String roleName);
    List<RoleDto> getAllRoles();
    void deleteRole(String roleName);
    List<PermissionDto> getRolePermissions(String roleName);
    void assignPermissionToRole(String roleName, String permissionName);
    void unassignPermissionToRole(String roleName, String permissionName);
}

