package com.example.blog_app.models.dtos.auth.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for the response of the Register API.
 *
 * <p>Contains the information of the registered user to be sent back
 * to the client upon successful registration.</p>
 */
@Getter
@Setter
@AllArgsConstructor
public class RegisterResponseDto {

    /**
     * The unique identifier of the registered user.
     */
    private Long id;

    /**
     * The name of the registered user.
     */
    private String name;

    /**
     * The email of the registered user.
     */
    private String email;

    /**
     * A brief message indicating the success of the registration.
     */
    private String message;
}
