package com.example.blog_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security.
 *
 * <p>This class defines the security configurations for the application,
 * including request authorization and password encoding mechanisms.</p>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Disables CSRF protection for simplicity (not recommended for production).</li>
 *   <li>Allows public access to specific endpoints such as user-related APIs.</li>
 *   <li>Requires authentication for all other endpoints.</li>
 *   <li>Provides a {@link BCryptPasswordEncoder} bean for secure password hashing.</li>
 * </ul>
 *
 * <p>Example usage in a service or controller:</p>
 * <pre>
 * {@code
 * @Autowired
 * private BCryptPasswordEncoder passwordEncoder;
 *
 * String hashedPassword = passwordEncoder.encode("plaintextPassword");
 * }
 * </pre>
 *
 * @see SecurityFilterChain
 * @see BCryptPasswordEncoder
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * <p>Disables CSRF protection and defines authorization rules:
     * <ul>
     *   <li>Allows public access to <code>/api/users/**</code>, <code>/api/roles/**</code>, and <code>/api/permissions/**</code>.</li>
     *   <li>Requires authentication for all other requests.</li>
     * </ul>
     * </p>
     *
     * @param http the {@link HttpSecurity} object used to configure security
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/roles/**").permitAll()
                        .requestMatchers("/api/permissions/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()
                        .requestMatchers("/api/series/**").permitAll()
                        .requestMatchers("/api/blogs/**").permitAll()
                        .anyRequest().authenticated() // Yêu cầu xác thực cho các endpoint khác
                );
        return http.build();
    }

    /**
     * Provides a bean for encoding passwords using BCrypt.
     *
     * <p>This bean is used throughout the application to securely hash passwords before storing them in the database.</p>
     *
     * @return a {@link BCryptPasswordEncoder} instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
