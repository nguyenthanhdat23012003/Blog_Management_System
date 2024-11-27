package com.example.blog_app.models.dtos.auth.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for user registration requests.
 *
 * <p>This class encapsulates the required fields for registering a new user
 * and applies validation constraints to ensure data integrity.</p>
 *
 * <p>Validation rules:</p>
 * <ul>
 *   <li><b>Name</b>: Cannot be empty and must not exceed 100 characters.</li>
 *   <li><b>Email</b>: Cannot be empty and must be a valid email address.</li>
 *   <li><b>Password</b>: Cannot be empty and must be at least 6 characters long.</li>
 * </ul>
 */
@Getter
@Setter
public class RegisterRequestDto {

    /**
     * The name of the user.
     *
     * <p>Validation:</p>
     * <ul>
     *   <li>Cannot be empty.</li>
     *   <li>Must not exceed 100 characters.</li>
     * </ul>
     */
    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    /**
     * The email of the user.
     *
     * <p>Validation:</p>
     * <ul>
     *   <li>Cannot be empty.</li>
     *   <li>Must be a valid email address format.</li>
     * </ul>
     */
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The password of the user.
     *
     * <p>Validation:</p>
     * <ul>
     *   <li>Cannot be empty.</li>
     *   <li>Must be at least 6 characters long.</li>
     * </ul>
     */
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
