package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ImmutableResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.role.RoleRequestDto;
import com.example.blog_app.models.dtos.role.RoleResponseDto;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.models.mappers.RoleMapper;
import com.example.blog_app.repositories.PermissionRepository;
import com.example.blog_app.repositories.RoleRepository;
import com.example.blog_app.services.RoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    /**
     * Constructs the RoleServiceImpl with required dependencies.
     *
     * @param roleRepository       the repository for managing roles
     * @param permissionRepository the repository for managing permissions
     * @param roleMapper           the mapper for converting Role entities to/from DTOs
     */
    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * Creates a new role.
     *
     * @param roleRequestDto the DTO containing role details for creation
     * @return the created role as a DTO
     * @throws DuplicateResourceException if the role already exists
     */
    @Override
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
        if (roleRepository.findByName(roleRequestDto.getName()).isPresent()) {
            throw new DuplicateResourceException("Role already exists: " + roleRequestDto.getName());
        }

        Role role = roleMapper.toEntity(roleRequestDto);
        handlePermissions(role, roleRequestDto.getPermissionIds());
        Role savedRole = roleRepository.save(role);
        return roleMapper.toResponseDto(savedRole);
    }

    /**
     * Updates an existing role.
     *
     * @param roleRequestDto the DTO containing updated role details
     * @param roleId the id of the role to update
     * @return the updated role as a DTO
     * @throws ResourceNotFoundException  if the role does not exist
     * @throws ImmutableResourceException if the role is immutable
     */
    @Override
    public RoleResponseDto updateRoleById(RoleRequestDto roleRequestDto, Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        if (role.isImmutable()) {
            throw new ImmutableResourceException("Cannot modify default role: " + role.getName());
        }

        roleMapper.updateRoleFromDto(roleRequestDto, role);
        handlePermissions(role, roleRequestDto.getPermissionIds());
        Role updatedRole = roleRepository.save(role);

        return roleMapper.toResponseDto(updatedRole);
    }

    /**
     * Retrieves the details of a role by its ID.
     *
     * <p>Fetches the role's information, including its associated permissions.</p>
     *
     * @param roleId the ID of the role to retrieve
     * @return the role's details as a response DTO
     * @throws ResourceNotFoundException if the role does not exist
     */
    @Override
    public RoleResponseDto getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));
        return roleMapper.toResponseDto(role);
    }

    /**
     * Retrieves all roles.
     *
     * @return a list of role DTOs representing all roles
     */
    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a role by its name.
     *
     * @param roleId the id of the role to delete
     * @throws ResourceNotFoundException  if the role does not exist
     * @throws ImmutableResourceException if the role is immutable
     */
    @Override
    public void deleteRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        if (role.isImmutable()) {
            throw new ImmutableResourceException("Cannot remove default role: " + role.getName());
        }

        roleRepository.delete(role);
    }

    /**
     * Handles the assignment of permissions to a role.
     *
     * <p>This method validates the list of permission IDs, ensures all provided IDs exist,
     * and assigns the corresponding permissions to the role. If any invalid IDs are found,
     * a {@link ResourceNotFoundException} is thrown with the list of invalid IDs.</p>
     *
     * @param role        the role entity to which permissions are assigned
     * @param permissionIds the list of permission IDs to assign to the role
     * @throws ResourceNotFoundException if any permission IDs are invalid
     */
    private void handlePermissions(Role role, Set<Long> permissionIds) {
        if(permissionIds != null && !permissionIds.isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));

            Set<Long> invalidPermissionIds = permissionIds.stream()
                    .filter(permissionId -> permissions.stream().noneMatch(permission -> permission.getId().equals(permissionId)))
                    .collect(Collectors.toSet());

            if (!invalidPermissionIds.isEmpty()) {
                throw new ResourceNotFoundException("Permissions not found with IDs: " + invalidPermissionIds);
            }

            role.setPermissions(permissions);
        }
    }
}
