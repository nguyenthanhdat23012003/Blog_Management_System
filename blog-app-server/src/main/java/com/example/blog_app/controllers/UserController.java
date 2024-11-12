package com.example.blog_app.controllers;

import com.example.blog_app.models.dtos.UserDto;
import com.example.blog_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createUserDto = this.userService.createUser(userDto);
        return ResponseEntity.ok().body(createUserDto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable int userId) {
        UserDto updatedUser = this.userService.updateUser(userDto, userId);
        return ResponseEntity.ok().body(updatedUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int userId) {
        UserDto userDto = this.userService.getUser(userId);
        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.ok().body("Delete user with id " + userId + " successfully");
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = this.userService.getAllUsers();
        return ResponseEntity.ok().body(userDtos);
    }
}
