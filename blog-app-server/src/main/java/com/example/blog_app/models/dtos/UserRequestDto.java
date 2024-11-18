package com.example.blog_app.models.dtos;

import com.example.blog_app.common.validation.CreateValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {
    private int id;

    @NotEmpty(message = "Name cannot be empty", groups = CreateValidationGroup.class)
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotEmpty(message = "Email cannot be empty", groups = CreateValidationGroup.class)
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password cannot be empty", groups = CreateValidationGroup.class)
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Size(max = 500, message = "About section cannot exceed 500 characters")
    private String about;
}
