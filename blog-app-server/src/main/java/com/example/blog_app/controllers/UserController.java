package com.example.blog_app.controllers;

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
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserRequestDto userDto, @PathVariable Integer userId) {
        UserResponseDto updatedUser = userService.updateUser(userDto, userId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer userId) {
        UserResponseDto user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
