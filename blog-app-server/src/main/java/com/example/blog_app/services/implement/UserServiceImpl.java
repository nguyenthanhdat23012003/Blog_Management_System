package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ImmutableResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.UserRequestDto;
import com.example.blog_app.models.dtos.UserResponseDto;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.models.mappers.UserMapper;
import com.example.blog_app.repositories.RoleRepository;
import com.example.blog_app.repositories.UserRepository;
import com.example.blog_app.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link UserService} interface for managing users.
 *
 * <p>This service provides business logic for user-related operations,
 * including creating, updating, retrieving, and deleting users, as well as
 * assigning roles to users.</p>
 *
 * <p>All interactions with the database are managed through repositories,
 * while domain-specific logic (e.g., handling immutable users) is implemented
 * within this service.</p>
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Constructs a new instance of {@link UserServiceImpl} with the required dependencies.
     *
     * @param userRepository   the repository for accessing user data
     * @param roleRepository   the repository for accessing role data
     * @param passwordEncoder  the password encoder for encrypting user passwords
     * @param userMapper       the mapper for converting between User entities and DTOs
     */
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Creates a new user in the system.
     *
     * <p>Validates that the user's email is unique, hashes the password, and
     * assigns the specified roles before saving the user to the database.</p>
     *
     * @param userDto the DTO containing the user's details
     * @return the created user's details as a {@link UserResponseDto}
     * @throws DuplicateResourceException if a user with the given email already exists
     */
    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists: " + userDto.getEmail());
        }
        User user = userMapper.toEntity(userDto);
        handlePassword(user, userDto.getPassword());
        handleRoles(user, userDto.getRoleIds());
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    /**
     * Updates an existing user's details.
     *
     * <p>Updates the specified fields from the provided DTO, while retaining the
     * user's existing data for fields not specified. If the user is marked as
     * immutable, an exception is thrown.</p>
     *
     * @param userDto the DTO containing the updated user details
     * @param userId  the ID of the user to update
     * @return the updated user's details as a {@link UserResponseDto}
     * @throws ResourceNotFoundException  if the user with the given ID does not exist
     * @throws ImmutableResourceException if the user is immutable
     */
    @Override
    public UserResponseDto updateUserById(UserRequestDto userDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        if (user.isImmutable()) {
            throw new ImmutableResourceException("User immutable");
        }
        userMapper.updateUserFromDto(userDto, user);
        handlePassword(user, userDto.getPassword());
        handleRoles(user, userDto.getRoleIds());
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    /**
     * Retrieves the details of a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user's details as a {@link UserResponseDto}
     * @throws ResourceNotFoundException if the user with the given ID does not exist
     */
    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return userMapper.toResponseDto(user);
    }

    /**
     * Retrieves all users from the system.
     *
     * @return a list of all users as {@link UserResponseDto}
     */
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a user by their ID.
     *
     * <p>If the user is marked as immutable, an exception is thrown to prevent deletion.</p>
     *
     * @param userId the ID of the user to delete
     * @throws ResourceNotFoundException  if the user with the given ID does not exist
     * @throws ImmutableResourceException if the user is immutable
     */
    @Override
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        if (user.isImmutable()) {
            throw new ImmutableResourceException("User cannot be deleted because it is immutable");
        }
        userRepository.delete(user);
    }

    /**
     * Encrypts the user's password if a new password is provided.
     *
     * <p>This method ensures that the password is hashed before being stored in the database.</p>
     *
     * @param user     the user entity to update
     * @param password the raw password to encrypt
     */
    private void handlePassword(User user, String password) {
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
    }

    /**
     * Assigns roles to the user based on the provided list of role IDs.
     *
     * <p>Each role ID is validated against the database to ensure it exists before
     * being assigned to the user.</p>
     *
     * @param user    the user entity to update
     * @param roleIds the list of role IDs to assign
     * @throws ResourceNotFoundException if any of the provided role IDs do not exist
     */
    private void handleRoles(User user, Set<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = roleIds.stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
    }
}
