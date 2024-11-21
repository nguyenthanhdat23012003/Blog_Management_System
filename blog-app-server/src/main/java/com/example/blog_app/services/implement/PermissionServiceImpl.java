package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ImmutableResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.PermissionDto;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.repositories.PermissionRepository;
import com.example.blog_app.services.PermissionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link PermissionService} interface for managing permissions.
 *
 * <p>This service provides the business logic for creating, updating, retrieving, and deleting permissions.</p>
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs the PermissionServiceImpl with required dependencies.
     *
     * @param permissionRepository the repository for managing permissions
     * @param modelMapper          the model mapper for DTO and entity conversion
     */
    public PermissionServiceImpl(PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new permission.
     *
     * @param permissionDto the DTO containing permission details for creation
     * @return the created permission as a DTO
     * @throws DuplicateResourceException if the permission already exists
     */
    @Override
    public PermissionDto createPermission(PermissionDto permissionDto) {
        if (permissionRepository.findByName(permissionDto.getName()).isPresent()) {
            throw new DuplicateResourceException("Permission already exists: " + permissionDto.getName());
        }

        Permission permission = this.dtoToPermission(permissionDto);
        Permission savedPermission = permissionRepository.save(permission);
        return this.permissionToDto(savedPermission);
    }

    /**
     * Updates an existing permission.
     *
     * @param permissionDto   the DTO containing updated permission details
     * @param permissionName  the name of the permission to update
     * @return the updated permission as a DTO
     * @throws ResourceNotFoundException  if the permission does not exist
     * @throws ImmutableResourceException if the permission is immutable
     */
    @Override
    public PermissionDto updatePermission(PermissionDto permissionDto, String permissionName) {
        if (permissionRepository.findByName(permissionName).isEmpty()) {
            throw new ResourceNotFoundException("Permission does not exist: " + permissionName);
        }

        if (permissionRepository.findByName(permissionName).get().isImmutable()) {
            throw new ImmutableResourceException("Cannot modify default permission: " + permissionName);
        }

        Permission permission = permissionRepository.findByName(permissionName).get();
        permission.setName(permissionDto.getName());
        Permission savedPermission = permissionRepository.save(permission);
        return this.permissionToDto(savedPermission);
    }

    /**
     * Retrieves all permissions.
     *
     * @return a list of permission DTOs representing all permissions
     */
    @Override
    public List<PermissionDto> getAllPermissions() {
        return this.permissionRepository.findAll()
                .stream()
                .map(this::permissionToDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a permission by its name.
     *
     * @param permissionName the name of the permission to delete
     * @throws ResourceNotFoundException  if the permission does not exist
     * @throws ImmutableResourceException if the permission is immutable
     */
    @Override
    public void deletePermission(String permissionName) {
        if (permissionRepository.findByName(permissionName).isEmpty()) {
            throw new ResourceNotFoundException("Permission does not exist: " + permissionName);
        }

        if (permissionRepository.findByName(permissionName).get().isImmutable()) {
            throw new ImmutableResourceException("Cannot remove default permission: " + permissionName);
        }

        Permission permission = permissionRepository.findByName(permissionName).get();
        permissionRepository.delete(permission);
    }

    /**
     * Converts a {@link Permission} entity to a {@link PermissionDto}.
     *
     * @param permission the permission entity to convert
     * @return the converted DTO
     */
    private PermissionDto permissionToDto(Permission permission) {
        return modelMapper.map(permission, PermissionDto.class);
    }

    /**
     * Converts a {@link PermissionDto} to a {@link Permission} entity.
     *
     * @param permissionDto the DTO to convert
     * @return the converted entity
     */
    private Permission dtoToPermission(PermissionDto permissionDto) {
        return modelMapper.map(permissionDto, Permission.class);
    }
}
