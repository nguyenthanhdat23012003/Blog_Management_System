package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ImmutableResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.RoleDto;
import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.repositories.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists: " + userDto.getEmail());
        }

        User user = this.dtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Hash password
        User savedUser = this.userRepository.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto userDto, Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if(user.isImmutable()) {
            throw new ImmutableResourceException("User immutable");
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isEmpty()) {
            user.setName(userDto.getName());
        }
        if (userDto.getAbout() != null && !userDto.getAbout().isEmpty()) {
            user.setAbout(userDto.getAbout());
        }
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        User updatedUser = this.userRepository.save(user);
        return this.userToDto(updatedUser);
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
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
    public void deleteUser(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if(user.isImmutable()) {
            throw new ImmutableResourceException("User immutable");
        }

        this.userRepository.delete(user);
    }

    @Override
    public List<RoleDto> getUserRoles(Long userId) {
        User user = this.userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return user.getRoles().stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void assignRoleToUser(Long userId, String roleName){
        User user = this.userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Role role = this.roleRepository.findByName(roleName).
                orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleName));
        user.getRoles().add(role);
        this.userRepository.save(user);
    }

    @Override
    public void unassignRoleFromUser(Long userId, String roleName) {
        User user = this.userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Role role = this.roleRepository.findByName(roleName).
                orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleName));
        user.getRoles().remove(role);
        this.userRepository.save(user);
    }

    private User dtoToUser(UserRequestDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserResponseDto userToDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}
