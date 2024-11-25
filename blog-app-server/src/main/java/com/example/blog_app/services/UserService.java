package com.example.blog_app.services;

import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;

import java.util.List;

/**
 * Service interface for managing user-related operations.
 * Defines the business logic for CRUD operations and role assignments.
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
    UserResponseDto updateUserById(UserRequestDto userDto, Long userId);

    /**
     * Retrieves the details of a specific user.
     *
     * @param userId the ID of the user to retrieve
     * @return the user's details as a response DTO
     */
    UserResponseDto getUserById(Long userId);

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
    void deleteUserById(Long userId);
}
