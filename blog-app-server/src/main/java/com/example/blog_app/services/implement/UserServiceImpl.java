package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ImmutableResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.exceptions.UnauthorizedException;
import com.example.blog_app.models.dtos.user.UserRequestDto;
import com.example.blog_app.models.dtos.user.UserResponseDto;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.models.mappers.UserMapper;
import com.example.blog_app.repositories.RoleRepository;
import com.example.blog_app.repositories.UserRepository;
import com.example.blog_app.security.SecurityUtils;
import com.example.blog_app.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
 * <p>Domain-specific validations (e.g., immutable users or permission checks)
 * are applied before persisting data to the database.</p>
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
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
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
     * <p>Applies validation to check if the user is immutable or if the current user
     * has the necessary permissions to perform the update.</p>
     *
     * @param userDto the DTO containing the updated user details
     * @param userId  the ID of the user to update
     * @return the updated user's details as a {@link UserResponseDto}
     * @throws ResourceNotFoundException  if the user with the given ID does not exist
     * @throws ImmutableResourceException if the user is immutable
     * @throws UnauthorizedException      if the current user lacks permission
     */
    @Override
    public UserResponseDto updateUserById(UserRequestDto userDto, Long userId) {
        User user = getUserEntityById(userId);
        checkImmutability(user);
        User currentUser = getCurrentUser();
        validatePermissionToUpdate(user, currentUser);
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
        User user = getUserEntityById(userId);
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
     * <p>Validates the user's immutability status before performing deletion.</p>
     *
     * @param userId the ID of the user to delete
     * @throws ResourceNotFoundException  if the user with the given ID does not exist
     * @throws ImmutableResourceException if the user is immutable
     */
    @Override
    public void deleteUserById(Long userId) {
        User user = getUserEntityById(userId);
        checkImmutability(user);
        userRepository.delete(user);
    }

    /**
     * Encrypts the user's password if a new password is provided.
     *
     * <p>Ensures that the password is securely hashed before persisting.</p>
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
     * <p>Validates the existence of roles in the database and assigns them to the user.</p>
     *
     * @param user    the user entity to update
     * @param roleIds the list of role IDs to assign
     * @throws ResourceNotFoundException if any of the provided role IDs do not exist
     */
    private void handleRoles(User user, Set<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
            Set<Long> invalidRoleIds = roleIds.stream()
                    .filter(roleId -> roles.stream().noneMatch(role -> role.getId().equals(roleId)))
                    .collect(Collectors.toSet());
            if (!invalidRoleIds.isEmpty()) {
                throw new ResourceNotFoundException("Roles not found with IDs: " + invalidRoleIds);
            }
            user.setRoles(roles);
        }
    }

    /**
     * Validates if the current user has permission to update another user's details.
     *
     * <p>Only admins or the user themselves can perform updates.</p>
     *
     * @param userToBeUpdated the user to be updated
     * @param currentUser     the current authenticated user
     * @throws UnauthorizedException if the current user lacks permission
     */
    private void validatePermissionToUpdate(User userToBeUpdated, User currentUser) {
        if (!isAdmin(currentUser) && !(userToBeUpdated.getId() == currentUser.getId())) {
            throw new UnauthorizedException("You do not have permission to update this user.");
        }
    }

    /**
     * Checks if the given user is an admin.
     *
     * @param user the user entity to check
     * @return {@code true} if the user has the ADMIN role, otherwise {@code false}
     */
    private boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the current user entity
     * @throws ResourceNotFoundException if the current user cannot be found
     */
    private User getCurrentUser() {
        String currentUserEmail = SecurityUtils.getCurrentUserEmail();
        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found with email: " + currentUserEmail));
    }

    /**
     * Retrieves a user entity by its ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user entity
     * @throws ResourceNotFoundException if the user with the given ID does not exist
     */
    private User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Validates if the user is immutable and throws an exception if so.
     *
     * @param user the user entity to check
     * @throws ImmutableResourceException if the user is immutable
     */
    private void checkImmutability(User user) {
        if (user.isImmutable()) {
            throw new ImmutableResourceException("User is immutable and cannot be modified.");
        }
    }
}
