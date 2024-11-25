package com.example.blog_app.models.mappers;

import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.models.entities.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for transforming {@link User} entities to DTOs and vice versa.
 *
 * <p>This interface uses MapStruct for mapping between entity and DTO layers,
 * providing a clean and efficient way to handle object transformation.</p>
 *
 * <p>Specific transformation logic, such as mapping between {@code Role} and
 * {@code roleIds}, is implemented through custom methods annotated with {@link Named}.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * User user = new User();
 * UserResponseDto responseDto = userMapper.toResponseDto(user);
 * }
 * </pre>
 *
 * @see User
 * @see UserRequestDto
 * @see UserResponseDto
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * Maps a {@link User} entity to a {@link UserResponseDto}.
     *
     * @param user the user entity to map
     * @return the mapped response DTO
     */
    @Mapping(target = "roleIds", source = "roles", qualifiedByName = "rolesToRoleIds")
    UserResponseDto toResponseDto(User user);

    /**
     * Maps a {@link UserRequestDto} to a {@link User} entity.
     *
     * @param dto the user request DTO to map
     * @return the mapped user entity
     */
    @Mapping(target = "roles", source = "roleIds", qualifiedByName = "roleIdsToRoles")
    User toEntity(UserRequestDto dto);

    /**
     * Updates an existing {@link User} entity with values from a {@link UserRequestDto}.
     *
     * <p>Note: Roles are ignored and should be handled in the service layer.</p>
     *
     * @param userDto the DTO containing updated user data
     * @param user    the existing user entity to update
     */
    @Mapping(target = "roles", ignore = true)
    void updateUserFromDto(UserRequestDto userDto, @MappingTarget User user);

    /**
     * Custom mapping to convert a set of {@link Role} entities to a set of role IDs.
     *
     * @param roles the set of roles to map
     * @return a set of role IDs
     */
    @Named("rolesToRoleIds")
    default Set<Long> mapRolesToRoleIds(Set<Role> roles) {
        return roles.stream().map(Role::getId).collect(Collectors.toSet());
    }

    /**
     * Custom mapping to convert a set of role IDs to a set of {@link Role} entities.
     *
     * <p>This is a simple transformation where only the ID is set for each role.
     * Full role data should be resolved in the service layer if required.</p>
     *
     * @param roleIds the set of role IDs to map
     * @return a set of roles with IDs populated
     */
    @Named("roleIdsToRoles")
    default Set<Role> mapRoleIdsToRoles(Set<Long> roleIds) {
        return roleIds.stream().map(id -> {
            Role role = new Role();
            role.setId(id);
            return role;
        }).collect(Collectors.toSet());
    }
}
