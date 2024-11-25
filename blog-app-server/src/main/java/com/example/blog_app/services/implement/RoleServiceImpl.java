package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ImmutableResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.repositories.PermissionRepository;
import com.example.blog_app.repositories.RoleRepository;
import com.example.blog_app.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link RoleService} interface for managing roles.
 *
 * <p>This service provides the business logic for creating, updating,
 * retrieving, and deleting roles, as well as assigning or unassigning permissions to roles.</p>
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PermissionRepository permissionRepository;

    /**
     * Constructs the RoleServiceImpl with required dependencies.
     *
     * @param roleRepository       the repository for managing roles
     * @param modelMapper          the model mapper for DTO and entity conversion
     * @param permissionRepository the repository for managing permissions
     */
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.permissionRepository = permissionRepository;
    }

    /**
     * Creates a new role.
     *
     * @param roleDto the DTO containing role details for creation
     * @return the created role as a DTO
     * @throws DuplicateResourceException if the role already exists
     */
    @Override
    public RoleDto createRole(RoleDto roleDto) {
        if (roleRepository.findByName(roleDto.getName()).isPresent()) {
            throw new DuplicateResourceException("Role already exists: " + roleDto.getName());
        }

        Role role = this.dtoToRole(roleDto);
        Role savedRole = roleRepository.save(role);
        return this.roleToDto(savedRole);
    }

    /**
     * Updates an existing role.
     *
     * @param roleDto  the DTO containing updated role details
     * @param roleName the name of the role to update
     * @return the updated role as a DTO
     * @throws ResourceNotFoundException  if the role does not exist
     * @throws ImmutableResourceException if the role is immutable
     */
    @Override
    public RoleDto updateRole(RoleDto roleDto, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role does not exist: " + roleDto.getName()));

        if (role.isImmutable()) {
            throw new ImmutableResourceException("Cannot modify default role: " + roleName);
        }

        role.setName(roleDto.getName());
        Role savedRole = roleRepository.save(role);
        return this.roleToDto(savedRole);
    }

    /**
     * Retrieves all roles.
     *
     * @return a list of role DTOs representing all roles
     */
    @Override
    public List<RoleDto> getAllRoles() {
        return this.roleRepository.findAll()
                .stream()
                .map(this::roleToDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a role by its name.
     *
     * @param roleName the name of the role to delete
     * @throws ResourceNotFoundException  if the role does not exist
     * @throws ImmutableResourceException if the role is immutable
     */
    @Override
    public void deleteRole(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role does not exist: " + roleName));

        if (role.isImmutable()) {
            throw new ImmutableResourceException("Cannot remove default role: " + roleName);
        }

        roleRepository.delete(role);
    }

    /**
     * Retrieves the permissions assigned to a specific role.
     *
     * @param roleName the name of the role
     * @return a list of permission DTOs assigned to the role
     */
    @Override
    public List<PermissionDto> getRolePermissions(String roleName) {
        Role role = this.roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        return role.getPermissions().stream()
                .map(permission -> modelMapper.map(permission, PermissionDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Assigns a permission to a role.
     *
     * @param roleName       the name of the role
     * @param permissionName the name of the permission to assign
     * @throws ResourceNotFoundException if the role or permission does not exist
     */
    @Override
    public void assignPermissionToRole(String roleName, String permissionName) {
        Role role = this.roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        Permission permission = this.permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with name: " + permissionName));

        role.getPermissions().add(permission);
        this.roleRepository.save(role);
    }

    /**
     * Unassigns a permission from a role.
     *
     * @param roleName       the name of the role
     * @param permissionName the name of the permission to unassign
     * @throws ResourceNotFoundException if the role or permission does not exist
     */
    @Override
    public void unassignPermissionToRole(String roleName, String permissionName) {
        Role role = this.roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        Permission permission = this.permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with name: " + permissionName));

        role.getPermissions().remove(permission);
        this.roleRepository.save(role);
    }

    /**
     * Converts a {@link Role} entity to a {@link RoleDto}.
     *
     * @param role the role entity to convert
     * @return the converted DTO
     */
    private RoleDto roleToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }

    /**
     * Converts a {@link RoleDto} to a {@link Role} entity.
     *
     * @param roleDto the DTO to convert
     * @return the converted entity
     */
    private Role dtoToRole(RoleDto roleDto) {
        return modelMapper.map(roleDto, Role.class);
    }
}
