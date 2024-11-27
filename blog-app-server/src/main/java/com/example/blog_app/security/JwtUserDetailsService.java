package com.example.blog_app.security;

import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserDetailsService} to load user-specific data during authentication.
 *
 * <p>This service is used by Spring Security to fetch user details such as roles
 * and permissions from the database based on the provided username (email).</p>
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs the {@link JwtUserDetailsService} with the required dependencies.
     *
     * @param userRepository the repository for accessing user data
     */
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user details by the provided username (email).
     *
     * <p>This method retrieves the user's roles and permissions from the database
     * and constructs a {@link UserDetails} object for authentication and authorization.</p>
     *
     * @param email the email of the user to load
     * @return the {@link UserDetails} object containing user information and authorities
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve user from the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Map roles and permissions to authorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName())) // Use permission names
                .collect(Collectors.toSet());

        // Return a UserDetails object for Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
