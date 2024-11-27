package com.example.blog_app.controllers;

import com.example.blog_app.models.dtos.auth.login.LoginRequestDto;
import com.example.blog_app.models.dtos.auth.login.LoginResponseDto;
import com.example.blog_app.models.dtos.auth.register.RegisterRequestDto;
import com.example.blog_app.models.dtos.auth.register.RegisterResponseDto;
import com.example.blog_app.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related operations.
 *
 * <p>This controller exposes APIs for user registration and login.</p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructs the AuthController with required dependencies.
     *
     * @param authService the service handling authentication logic
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * API endpoint for user registration.
     *
     * <p>Accepts a {@link RegisterRequestDto} and returns a {@link RegisterResponseDto}
     * on successful registration.</p>
     *
     * @param requestDto the request DTO containing registration details
     * @return the response DTO containing the registered user's information
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        RegisterResponseDto responseDto = authService.register(requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }

    /**
     * API endpoint for user login.
     *
     * <p>Accepts a {@link LoginRequestDto} and returns a {@link LoginResponseDto}
     * containing the JWT token if the credentials are valid.</p>
     *
     * @param requestDto the request DTO containing login credentials
     * @return the response DTO containing the JWT token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }
}
