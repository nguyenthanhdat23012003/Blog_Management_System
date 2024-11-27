package com.example.blog_app.services;

import com.example.blog_app.models.dtos.auth.login.LoginRequestDto;
import com.example.blog_app.models.dtos.auth.login.LoginResponseDto;
import com.example.blog_app.models.dtos.auth.register.RegisterRequestDto;
import com.example.blog_app.models.dtos.auth.register.RegisterResponseDto;

/**
 * Service interface for handling authentication-related operations.
 *
 * <p>This interface defines the contract for registering users and
 * authenticating user credentials (login).</p>
 */
public interface AuthService {

    /**
     * Registers a new user.
     *
     * @param registerRequestDto the DTO containing user registration details
     * @return the response DTO containing registered user details
     */
    RegisterResponseDto register(RegisterRequestDto registerRequestDto);

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginRequestDto the DTO containing login credentials
     * @return the response DTO containing the JWT token and user details
     */
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
