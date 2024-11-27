package com.example.blog_app.models.dtos.auth.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing the login request payload.
 *
 * <p>Contains the required fields for authenticating a user.</p>
 */
@Getter
@Setter
public class LoginRequestDto {

    /**
     * The email of the user attempting to log in.
     *
     * <p>Must be a valid email format and cannot be empty.</p>
     */
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The password of the user attempting to log in.
     *
     * <p>Cannot be empty.</p>
     */
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
