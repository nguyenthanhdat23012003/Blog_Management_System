package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.DuplicateResourceException;
import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.exceptions.UnauthorizedException;
import com.example.blog_app.models.dtos.auth.login.LoginRequestDto;
import com.example.blog_app.models.dtos.auth.login.LoginResponseDto;
import com.example.blog_app.models.dtos.auth.register.RegisterRequestDto;
import com.example.blog_app.models.dtos.auth.register.RegisterResponseDto;
import com.example.blog_app.models.entities.Role;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.models.mappers.RoleMapper;
import com.example.blog_app.models.mappers.UserMapper;
import com.example.blog_app.repositories.RoleRepository;
import com.example.blog_app.repositories.UserRepository;
import com.example.blog_app.security.JwtTokenProvider;
import com.example.blog_app.services.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the {@link AuthService} interface for managing authentication logic.
 *
 * <p>This service handles user registration and login, including generating
 * JWT tokens for authenticated users.</p>
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;

    /**
     * Constructs the AuthServiceImpl with the required dependencies.
     *
     * @param userRepository   the repository for managing user data
     * @param passwordEncoder  the password encoder for hashing user passwords
     * @param jwtTokenProvider the provider for generating JWT tokens
     * @param roleRepository   the repository for managing role data
     */
    public AuthServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
    }

    /**
     * Registers a new user.
     *
     * <p>Assigns a default role and ensures email uniqueness before saving the user.</p>
     *
     * @param registerRequestDto the DTO containing user registration details
     * @return the response DTO containing registered user details
     * @throws DuplicateResourceException if the email is already in use
     * @throws ResourceNotFoundException if the default role cannot be found
     */
    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        // Check if email is already in use
        if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists: " + registerRequestDto.getEmail());
        }

        // Fetch the default role
        Role defaultRole = roleRepository.findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        // Create and save the user
        User user = new User();
        user.setName(registerRequestDto.getName());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        return new RegisterResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                "User registered successfully!"
        );
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * <p>Validates the email and password before generating a JWT token.</p>
     *
     * @param loginRequestDto the DTO containing login credentials
     * @return the response DTO containing the JWT token and user details
     * @throws ResourceNotFoundException if the user is not found by email
     * @throws UnauthorizedException if the password is invalid
     */
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // Find the user by email
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

        // Verify the password
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        // Generate a JWT token
        String token = jwtTokenProvider.generateToken(user);

        // Return the login response
        return new LoginResponseDto(token, user.getEmail());
    }
}
