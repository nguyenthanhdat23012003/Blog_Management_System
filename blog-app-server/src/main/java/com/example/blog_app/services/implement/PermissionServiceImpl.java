package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ImmutableResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.PermissionRequestDto;
import com.example.blog_app.models.dtos.PermissionResponseDto;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.models.mappers.PermissionMapper;
import com.example.blog_app.repositories.PermissionRepository;
import com.example.blog_app.services.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link PermissionService} interface for managing permissions.
 *
 * <p>This service provides the business logic for creating, updating, retrieving,
 * and deleting permissions. Immutable permissions cannot be modified or deleted.</p>
 *
 * @see PermissionService
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    /**
     * Constructs the PermissionServiceImpl with required dependencies.
     *
     * @param permissionRepository the repository for managing permissions
     * @param permissionMapper     the mapper for converting between entities and DTOs
     */
    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    /**
     * Creates a new permission.
     *
     * @param permissionDto the DTO containing the details of the permission to create
     * @return the created permission as a {@link PermissionResponseDto}
     * @throws DuplicateResourceException if a permission with the same name already exists
     */
    @Override
    public PermissionResponseDto createPermission(PermissionRequestDto permissionDto) {
        if (permissionRepository.findByName(permissionDto.getName()).isPresent()) {
            throw new DuplicateResourceException("Permission already exists with name: " + permissionDto.getName());
        }

        Permission permission = permissionMapper.toEntity(permissionDto);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toResponseDto(savedPermission);
    }

    /**
     * Updates an existing permission by its ID.
     *
     * @param permissionDto the DTO containing the updated details of the permission
     * @param id            the ID of the permission to update
     * @return the updated permission as a {@link PermissionResponseDto}
     * @throws ResourceNotFoundException  if no permission is found with the given ID
     * @throws ImmutableResourceException if the permission is marked as immutable
     */
    @Override
    public PermissionResponseDto updatePermissionById(PermissionRequestDto permissionDto, Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + id));

        if (permission.isImmutable()) {
            throw new ImmutableResourceException("Cannot modify immutable permission with ID: " + id);
        }

        permission.setName(permissionDto.getName());
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.toResponseDto(updatedPermission);
    }

    /**
     * Retrieves the details of a permission by its ID.
     *
     * @param id the ID of the permission to retrieve
     * @return the permission as a {@link PermissionResponseDto}
     * @throws ResourceNotFoundException if no permission is found with the given ID
     */
    @Override
    public PermissionResponseDto getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + id));
        return permissionMapper.toResponseDto(permission);
    }

    /**
     * Retrieves a list of all permissions.
     *
     * @return a list of all permissions as {@link PermissionResponseDto}
     */
    @Override
    public List<PermissionResponseDto> getAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toResponseDto)
                .toList();
    }

    /**
     * Deletes a permission by its ID.
     *
     * @param id the ID of the permission to delete
     * @throws ResourceNotFoundException  if no permission is found with the given ID
     * @throws ImmutableResourceException if the permission is marked as immutable
     */
    @Override
    public void deletePermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + id));

        if (permission.isImmutable()) {
            throw new ImmutableResourceException("Cannot delete immutable permission with ID: " + id);
        }

        permissionRepository.delete(permission);
    }
}
