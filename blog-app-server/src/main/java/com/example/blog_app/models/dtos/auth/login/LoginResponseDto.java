package com.example.blog_app.models.dtos.auth.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing the response for a successful login.
 *
 * <p>Contains the generated JWT token and user information.</p>
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {

    /**
     * The JWT token generated for the authenticated user.
     */
    private String token;

    /**
     * The email of the authenticated user.
     */
    private String email;
}
