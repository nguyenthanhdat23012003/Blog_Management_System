package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ImmutableResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.repositories.PermissionRepository;
import com.example.blog_app.repositories.RoleRepository;
import com.example.blog_app.repositories.UserRepository;
import com.example.blog_app.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        if (roleRepository.findByName(roleDto.getName()).isPresent()) {
            throw new DuplicateResourceException("Role already exists: " + roleDto.getName());
        }

        Role role = this.dtoToRole(roleDto);
        Role savedRole = roleRepository.save(role);
        return this.roleToDto(savedRole);
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto, String roleName) {
        if(roleRepository.findByName(roleName).isEmpty()){
            throw new ResourceNotFoundException("Role does not exist: " + roleDto.getName());
        }

        if(roleRepository.findByName(roleName).get().isImmutable()){
            throw new ImmutableResourceException("Can not modify default role: " + roleDto.getName());
        }

        Role role = roleRepository.findByName(roleName).get();
        role.setName(roleDto.getName());
        Role savedRole = roleRepository.save(role);
        return this.roleToDto(savedRole);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return this.roleRepository.findAll()
                .stream()
                .map(this::roleToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRole(String roleName) {
        if(roleRepository.findByName(roleName).isEmpty()){
            throw new ResourceNotFoundException("Role does not exist: " + roleName);
        }

        if(roleRepository.findByName(roleName).get().isImmutable()){
            throw new ImmutableResourceException("Can not remove default role: " + roleName);
        }

        Role role = roleRepository.findByName(roleName).get();
        roleRepository.delete(role);
    }

    @Override
    public List<PermissionDto> getRolePermissions(String roleName) {
        Role role = this.roleRepository.findByName(roleName).
                orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        return role.getPermissions().stream()
                .map(permission -> modelMapper.map(permission, PermissionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void assignPermissionToRole(String roleName, String permissionName) {
        Role role = this.roleRepository.findByName(roleName).
                orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        Permission permission = this.permissionRepository.findByName(permissionName).
                orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionName));
        role.getPermissions().add(permission);
        this.roleRepository.save(role);
    }

    @Override
    public void unassignPermissionToRole(String roleName, String permissionName) {
        Role role = this.roleRepository.findByName(roleName).
                orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        Permission permission = this.permissionRepository.findByName(permissionName).
                orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionName));
        role.getPermissions().remove(permission);
        this.roleRepository.save(role);
    }

    private RoleDto roleToDto(Role role){
        return modelMapper.map(role, RoleDto.class);
    }

    private Role dtoToRole(RoleDto roleDto){
        return modelMapper.map(roleDto, Role.class);
    }
}
