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

/**
 * Implementation of the {@link UserService} interface for managing users.
 *
 * <p>This service provides the business logic for user-related operations such as:</p>
 * <ul>
 *   <li>Creating a new user.</li>
 *   <li>Updating user information.</li>
 *   <li>Retrieving user details and roles.</li>
 *   <li>Deleting a user.</li>
 *   <li>Assigning or unassigning roles to/from users.</li>
 * </ul>
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /**
     * Constructs the UserServiceImpl with required dependencies.
     *
     * @param userRepository   the repository for managing users
     * @param roleRepository   the repository for managing roles
     * @param passwordEncoder  the encoder for hashing passwords
     * @param modelMapper      the model mapper for DTO and entity conversion
     */
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new user.
     *
     * @param userDto the DTO containing user details for creation
     * @return the created user's details as a response DTO
     * @throws DuplicateResourceException if a user with the same email already exists
     */
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

    /**
     * Updates an existing user's information.
     *
     * @param userDto the DTO containing updated user details
     * @param userId  the ID of the user to update
     * @return the updated user's details as a response DTO
     * @throws ResourceNotFoundException  if the user does not exist
     * @throws ImmutableResourceException if the user is immutable
     */
    @Override
    public UserResponseDto updateUser(UserRequestDto userDto, Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (user.isImmutable()) {
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

    /**
     * Retrieves the details of a specific user.
     *
     * @param userId the ID of the user to retrieve
     * @return the user's details as a response DTO
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    public UserResponseDto getUser(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return this.userToDto(user);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of user response DTOs representing all users
     */
    @Override
    public List<UserResponseDto> getAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @throws ResourceNotFoundException  if the user does not exist
     * @throws ImmutableResourceException if the user is immutable
     */
    @Override
    public void deleteUser(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (user.isImmutable()) {
            throw new ImmutableResourceException("User immutable");
        }

        this.userRepository.delete(user);
    }

    /**
     * Retrieves the roles assigned to a specific user.
     *
     * @param userId the ID of the user
     * @return a list of role DTOs assigned to the user
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    public List<RoleDto> getUserRoles(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return user.getRoles().stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId   the ID of the user
     * @param roleName the name of the role to assign
     * @throws ResourceNotFoundException if the user or role does not exist
     */
    @Override
    public void assignRoleToUser(Long userId, String roleName) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Role role = this.roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        user.getRoles().add(role);
        this.userRepository.save(user);
    }

    /**
     * Unassigns a role from a user.
     *
     * @param userId   the ID of the user
     * @param roleName the name of the role to unassign
     * @throws ResourceNotFoundException if the user or role does not exist
     */
    @Override
    public void unassignRoleFromUser(Long userId, String roleName) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Role role = this.roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
        user.getRoles().remove(role);
        this.userRepository.save(user);
    }

    /**
     * Converts a {@link UserRequestDto} to a {@link User} entity.
     *
     * @param userDto the DTO to convert
     * @return the converted entity
     */
    private User dtoToUser(UserRequestDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    /**
     * Converts a {@link User} entity to a {@link UserResponseDto}.
     *
     * @param user the user entity to convert
     * @return the converted DTO
     */
    private UserResponseDto userToDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}
