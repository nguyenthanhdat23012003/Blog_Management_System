package com.example.blog_app.services;

import com.example.blog_app.models.dtos.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer userId);
    UserDto getUser(Integer userId);
    List<UserDto> getAllUsers();
    void deleteUser(Integer userId);
}