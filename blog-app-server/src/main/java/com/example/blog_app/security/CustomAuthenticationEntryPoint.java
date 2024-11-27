package com.example.blog_app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom authentication entry point to handle unauthorized access.
 *
 * <p>This class is triggered whenever an unauthenticated user tries to access
 * a secured resource. It returns a custom JSON response with error details.</p>
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles unauthorized access attempts.
     *
     * <p>This method sets the HTTP response status to 401 (Unauthorized) and
     * writes a JSON object containing error details to the response body.</p>
     *
     * @param request       the HTTP request that resulted in an {@link AuthenticationException}
     * @param response      the HTTP response to write the error details
     * @param authException the exception that triggered this entry point
     * @throws IOException if an input or output error occurs while writing the response
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Set response content type and status
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a response body with error details
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "Unauthorized");
        responseBody.put("message", "You must provide a valid token to access this resource.");
        responseBody.put("timestamp", System.currentTimeMillis());

        // Write the JSON response body
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
