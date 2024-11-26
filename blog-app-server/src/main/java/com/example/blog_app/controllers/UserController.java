package com.example.blog_app.controllers;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.UpdateValidationGroup;
import com.example.blog_app.models.dtos.user.UserRequestDto;
import com.example.blog_app.models.dtos.user.UserResponseDto;
import com.example.blog_app.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing users in the application.
 * Provides endpoints for CRUD operations and role assignments.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a new {@link UserController} with the given {@link UserService}.
     *
     * @param userService the service to handle user-related business logic
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user.
     *
     * @param userDto the DTO containing user details
     * @return the created user's details as a {@link UserResponseDto}
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Validated({CreateValidationGroup.class}) @RequestBody UserRequestDto userDto) {
        UserResponseDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(201).body(createdUser);
    }

    /**
     * Updates an existing user.
     *
     * @param userDto the DTO containing updated user details
     * @param userId  the ID of the user to update
     * @return the updated user's details as a {@link UserResponseDto}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @Validated(UpdateValidationGroup.class) @RequestBody UserRequestDto userDto,
            @PathVariable Long userId) {
        UserResponseDto updatedUser = userService.updateUserById(userDto, userId);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user details as a {@link UserResponseDto}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users as {@link UserResponseDto}
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @return a confirmation message indicating the deletion
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
