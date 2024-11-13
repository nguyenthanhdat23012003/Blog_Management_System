package com.example.blog_app.controllers;

import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;
import com.example.blog_app.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userDto) {
        UserResponseDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(201).body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserRequestDto userDto, @PathVariable Long userId) {
        UserResponseDto updatedUser = userService.updateUser(userDto, userId);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto user = userService.getUser(userId);
        return ResponseEntity.status(200).body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<RoleDto>> getUserRoles(@PathVariable Long userId) {
        List<RoleDto> roles = userService.getUserRoles(userId);
        return ResponseEntity.status(200).body(roles);
    }

    @PostMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<String> assignRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
        userService.assignRoleToUser(userId, roleName);
        return ResponseEntity.ok("Assigned role to user successfully");
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<String> unassignRoleFromUser(@PathVariable Long userId, @PathVariable String roleName) {
        userService.unassignRoleFromUser(userId, roleName);
        return ResponseEntity.ok("Unassigned role from user successfully");
    }
}
