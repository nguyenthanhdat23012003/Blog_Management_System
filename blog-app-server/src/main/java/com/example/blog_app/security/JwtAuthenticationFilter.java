package com.example.blog_app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A custom filter for handling JWT-based authentication.
 *
 * <p>This filter intercepts incoming HTTP requests to extract and validate JWT tokens.
 * If a valid token is found, the filter sets the authentication context for the current request.</p>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Extracts the token from the Authorization header.</li>
 *   <li>Validates the token using {@link JwtTokenProvider}.</li>
 *   <li>Loads user details and sets authentication in {@link SecurityContextHolder}.</li>
 * </ul>
 *
 * <p>This filter is executed once per request as it extends {@link OncePerRequestFilter}.</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService userDetailsService;

    /**
     * Constructs a new instance of {@link JwtAuthenticationFilter} with the required dependencies.
     *
     * @param jwtTokenProvider   the provider for generating and validating JWT tokens
     * @param userDetailsService the service for loading user details
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, JwtUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Processes incoming HTTP requests to authenticate users based on JWT tokens.
     *
     * <p>If a valid JWT token is present, the user is authenticated, and the
     * security context is updated. If no token or an invalid token is present,
     * the request continues without authentication.</p>
     *
     * @param request     the incoming HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain to process subsequent filters
     * @throws ServletException if an error occurs during filtering
     * @throws IOException      if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the JWT token from the request
        String token = getJwtFromRequest(request);

        // Validate the token and set authentication context if valid
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            SecurityContextHolder.getContext().setAuthentication(
                    jwtTokenProvider.getAuthentication(token)
            );
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     *
     * <p>The Authorization header is expected to contain a Bearer token in the format:
     * {@code Bearer <token>}</p>
     *
     * @param request the incoming HTTP request
     * @return the extracted JWT token, or {@code null} if the header is missing or invalid
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7)
                : null;
    }
}
