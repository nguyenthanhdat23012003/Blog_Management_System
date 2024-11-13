package com.example.blog_app.services;

import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userDto);
    UserResponseDto updateUser(UserRequestDto userDto, Integer userId);
    UserResponseDto getUser(Integer userId);
    List<UserResponseDto> getAllUsers();
    void deleteUser(Integer userId);
}
