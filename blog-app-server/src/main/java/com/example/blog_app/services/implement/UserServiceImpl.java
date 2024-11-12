package com.example.blog_app.services.implement;

import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.repositories.UserRepository;
import com.example.blog_app.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }

        User user = this.dtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Hash password
        User savedUser = this.userRepository.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto userDto, Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepository.save(user);
        return this.userToDto(updatedUser);
    }

    @Override
    public UserResponseDto getUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return this.userToDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        this.userRepository.delete(user);
    }

    private User dtoToUser(UserRequestDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserResponseDto userToDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}
