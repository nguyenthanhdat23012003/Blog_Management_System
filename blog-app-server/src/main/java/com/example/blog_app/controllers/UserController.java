package com.example.blog_app.controllers;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.UpdateValidationGroup;
import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;
import com.example.blog_app.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for managing users in the application.
 *
 * <p>This controller provides endpoints for user creation, retrieval,
 * updating, and deletion. Additionally, it handles the assignment
 * and unassignment of roles to users.</p>
 *
 * <p>Example endpoints:</p>
 * <ul>
 *   <li>POST /api/users - Create a new user</li>
 *   <li>PUT /api/users/{userId} - Update an existing user</li>
 *   <li>GET /api/users/{userId} - Retrieve user details by ID</li>
 *   <li>GET /api/users - Retrieve all users</li>
 *   <li>DELETE /api/users/{userId} - Delete a user</li>
 *   <li>GET /api/users/{userId}/roles - Retrieve roles assigned to a user</li>
 *   <li>POST /api/users/{userId}/roles/{roleName} - Assign a role to a user</li>
 *   <li>DELETE /api/users/{userId}/roles/{roleName} - Unassign a role from a user</li>
 * </ul>
 *
 * @see UserService
 * @see UserRequestDto
 * @see UserResponseDto
 * @see RoleDto
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
     * @return the created user as a {@link UserResponseDto}
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Validated(CreateValidationGroup.class) @RequestBody UserRequestDto userDto) {
        UserResponseDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(201).body(createdUser);
    }

    /**
     * Updates an existing user.
     *
     * @param userDto the DTO containing updated user details
     * @param userId  the ID of the user to update
     * @return the updated user as a {@link UserResponseDto}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Validated(UpdateValidationGroup.class) @RequestBody UserRequestDto userDto,
            @PathVariable Long userId) {
        UserResponseDto updatedUser = userService.updateUser(userDto, userId);
        return ResponseEntity.status(200).body(updatedUser);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user details as a {@link UserResponseDto}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto user = userService.getUser(userId);
        return ResponseEntity.status(200).body(user);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users as {@link UserResponseDto}
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @return a confirmation message indicating the deletion
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * Retrieves all roles assigned to a user.
     *
     * @param userId the ID of the user
     * @return a list of roles assigned to the user as {@link RoleDto}
     */
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<RoleDto>> getUserRoles(@PathVariable Long userId) {
        List<RoleDto> roles = userService.getUserRoles(userId);
        return ResponseEntity.status(200).body(roles);
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId   the ID of the user
     * @param roleName the name of the role to assign
     * @return a confirmation message indicating the assignment
     */
    @PostMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<String> assignRoleToUser(
            @PathVariable Long userId, @PathVariable String roleName) {
        userService.assignRoleToUser(userId, roleName);
        return ResponseEntity.ok("Assigned role to user successfully");
    }

    /**
     * Unassigns a role from a user.
     *
     * @param userId   the ID of the user
     * @param roleName the name of the role to unassign
     * @return a confirmation message indicating the unassignment
     */
    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<String> unassignRoleFromUser(
            @PathVariable Long userId, @PathVariable String roleName) {
        userService.unassignRoleFromUser(userId, roleName);
        return ResponseEntity.ok("Unassigned role from user successfully");
    }
}
