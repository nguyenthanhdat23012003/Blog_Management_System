package com.example.blog_app.models.mappers;

import com.example.blog_app.models.dtos.role.RoleRequestDto;
import com.example.blog_app.models.dtos.role.RoleResponseDto;
import com.example.blog_app.models.entities.Permission;
import com.example.blog_app.models.entities.Role;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for transforming {@link Role} entities to DTOs and vice versa.
 *
 * <p>This interface uses MapStruct for mapping between entity and DTO layers,
 * providing a clean and efficient way to handle object transformation.</p>
 *
 * <p>Specific transformation logic, such as mapping between {@code Permission} and
 * {@code permissionIds}, is implemented through custom methods annotated with {@link Named}.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * Role role = new Role();
 * RoleResponseDto responseDto = roleMapper.toResponseDto(role);
 * }
 * </pre>
 *
 * @see Role
 * @see RoleRequestDto
 * @see RoleResponseDto
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    /**
     * Maps a {@link Role} entity to a {@link RoleResponseDto}.
     *
     * @param role the role entity to map
     * @return the mapped response DTO
     */
    @Mapping(target = "permissionIds", source = "permissions", qualifiedByName = "permissionsToPermissionIds")
    RoleResponseDto toResponseDto(Role role);

    /**
     * Maps a {@link RoleRequestDto} to a {@link Role} entity.
     *
     * @param dto the role request DTO to map
     * @return the mapped role entity
     */
    @Mapping(target = "permissions", source = "permissionIds", qualifiedByName = "permissionIdsToPermissions")
    Role toEntity(RoleRequestDto dto);

    /**
     * Updates an existing {@link Role} entity with values from a {@link RoleRequestDto}.
     *
     * <p>Note: Permissions are ignored and should be handled in the service layer.</p>
     *
     * @param roleDto the DTO containing updated role data
     * @param role    the existing role entity to update
     */
    @Mapping(target = "permissions", ignore = true)
    void updateRoleFromDto(RoleRequestDto roleDto, @MappingTarget Role role);

    /**
     * Custom mapping to convert a set of {@link Permission} entities to a set of permission IDs.
     *
     * @param permissions the set of permissions to map
     * @return a set of permission IDs
     */
    @Named("permissionsToPermissionIds")
    default Set<Long> mapPermissionsToPermissionIds(Set<Permission> permissions) {
        return permissions.stream().map(Permission::getId).collect(Collectors.toSet());
    }

    /**
     * Custom mapping to convert a set of permission IDs to a set of {@link Permission} entities.
     *
     * <p>This is a simple transformation where only the ID is set for each permission.
     * Full permission data should be resolved in the service layer if required.</p>
     *
     * @param permissionIds the set of permission IDs to map
     * @return a set of permissions with IDs populated
     */
    @Named("permissionIdsToPermissions")
    default Set<Permission> mapPermissionIdsToPermissions(Set<Long> permissionIds) {
        return permissionIds.stream().map(id -> {
            Permission permission = new Permission();
            permission.setId(id);
            return permission;
        }).collect(Collectors.toSet());
    }
}
