package com.example.blog_app.services;

import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;

import java.util.List;

/**
 * Service interface for managing user-related operations.
 *
 * <p>This interface defines the business logic for user management,
 * including CRUD operations and role assignments.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * UserService userService = new UserServiceImpl();
 * UserResponseDto user = userService.getUser(1L);
 * }
 * </pre>
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param userDto the DTO containing user details for creation
     * @return the created user's details as a response DTO
     */
    UserResponseDto createUser(UserRequestDto userDto);

    /**
     * Updates an existing user's details.
     *
     * @param userDto the DTO containing updated user details
     * @param userId  the ID of the user to be updated
     * @return the updated user's details as a response DTO
     */
    UserResponseDto updateUser(UserRequestDto userDto, Long userId);

    /**
     * Retrieves the details of a specific user.
     *
     * @param userId the ID of the user to retrieve
     * @return the user's details as a response DTO
     */
    UserResponseDto getUser(Long userId);

    /**
     * Retrieves a list of all users.
     *
     * @return a list of user response DTOs representing all users
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    void deleteUser(Long userId);

    /**
     * Retrieves the roles assigned to a specific user.
     *
     * @param userId the ID of the user
     * @return a list of role DTOs assigned to the user
     */
    List<RoleDto> getUserRoles(Long userId);

    /**
     * Assigns a role to a user.
     *
     * @param userId   the ID of the user
     * @param roleName the name of the role to assign
     */
    void assignRoleToUser(Long userId, String roleName);

    /**
     * Unassigns a role from a user.
     *
     * @param userId   the ID of the user
     * @param roleName the name of the role to unassign
     */
    void unassignRoleFromUser(Long userId, String roleName);
}
