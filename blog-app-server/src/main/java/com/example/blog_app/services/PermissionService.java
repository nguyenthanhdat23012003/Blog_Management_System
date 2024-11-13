package com.example.blog_app.services;

import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.entities.Permission;

import java.util.List;

public interface PermissionService {
    PermissionDto createPermission(PermissionDto permissionDto);
    PermissionDto updatePermission(PermissionDto permissionDto, String permissionName);
    List<PermissionDto> getAllPermissions();
    void deletePermission(String permissionName);
}

