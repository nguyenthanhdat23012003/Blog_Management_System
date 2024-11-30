package com.example.blog_app.config;

import com.example.blog_app.security.CustomAuthenticationEntryPoint;
import com.example.blog_app.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }
    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http the {@link HttpSecurity} object used to configure security
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Vô hiệu hóa CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/me").hasAuthority("VIEW_USER")
                        .requestMatchers("/api/auth/admin/me").hasAuthority("ADMINISTRATOR")
                        .requestMatchers("/api/roles/**").hasAuthority("ADMINISTRATOR")
                        .requestMatchers("/api/permissions/**").hasAuthority("ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/series/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/blogs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAuthority("VIEW_USER")

                        .requestMatchers(HttpMethod.POST,"/api/categories/**").hasAuthority("CREATE_CATEGORY")
                        .requestMatchers(HttpMethod.PUT,"/api/categories/**").hasAuthority("UPDATE_CATEGORY")
                        .requestMatchers(HttpMethod.DELETE,"/api/categories/**").hasAuthority("DELETE_CATEGORY")

                        .requestMatchers(HttpMethod.POST, "/api/series/**").hasAuthority("CREATE_SERIES")
                        .requestMatchers(HttpMethod.PUT, "/api/series/**").hasAuthority("UPDATE_SERIES")
                        .requestMatchers(HttpMethod.DELETE, "/api/series/**").hasAuthority("DELETE_SERIES")

                        .requestMatchers(HttpMethod.POST, "/api/blogs/**").hasAuthority("CREATE_BLOG")
                        .requestMatchers(HttpMethod.PUT, "/api/blogs/**").hasAuthority("UPDATE_BLOG")
                        .requestMatchers(HttpMethod.DELETE, "/api/blogs/**").hasAuthority("DELETE_BLOG")

                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasAuthority("CREATE_USER")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAuthority("UPDATE_USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("DELETE_USER")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Sử dụng Custom Entry Point
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);;
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Cho phép tất cả các nguồn
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(false); // Không sử dụng credentials như cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
