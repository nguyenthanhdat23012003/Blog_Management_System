package com.example.blog_app.services;

import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userDto);
    UserResponseDto updateUser(UserRequestDto userDto, Long userId);
    UserResponseDto getUser(Long userId);
    List<UserResponseDto> getAllUsers();
    void deleteUser(Long userId);
    List<RoleDto> getUserRoles(Long userId);
    void assignRoleToUser(Long userId, String roleName);
    void unassignRoleFromUser(Long userId, String roleName);
}
