package com.example.blog_app.models.dtos.user;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.GeneralValidationGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for handling user-related requests.
 *
 * <p>This class is used to capture and validate user input during operations
 * such as user creation and updates. It includes validation constraints
 * to ensure data integrity.</p>
 *
 * <p>Validation annotations are used to enforce rules, such as:</p>
 * <ul>
 *   <li>Name must be between 3 and 100 characters.</li>
 *   <li>Email must be in a valid format.</li>
 *   <li>Password must be at least 6 characters long.</li>
 *   <li>The "about" section cannot exceed 500 characters.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * UserRequestDto user = new UserRequestDto();
 * user.setName("John Doe");
 * user.setEmail("john.doe@example.com");
 * user.setPassword("securePassword123");
 * user.setAbout("A passionate software engineer.");
 * }
 * </pre>
 */
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = false)
public class UserRequestDto {

    /**
     * The name of the user.
     *
     * <p>Must be between 3 and 100 characters.</p>
     */
    @NotEmpty(message = "Name cannot be empty", groups = CreateValidationGroup.class)
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters", groups = GeneralValidationGroup.class)
    private String name;

    /**
     * The email address of the user.
     *
     * <p>Must be in a valid email format.</p>
     */
    @NotEmpty(message = "Email cannot be empty", groups = CreateValidationGroup.class)
    @Email(message = "Email should be valid", groups = GeneralValidationGroup.class)
    private String email;

    /**
     * The password for the user account.
     *
     * <p>Must be at least 6 characters long.</p>
     */
    @NotEmpty(message = "Password cannot be empty", groups = CreateValidationGroup.class)
    @Size(min = 6, message = "Password must be at least 6 characters long", groups = GeneralValidationGroup.class)
    private String password;

    /**
     * A brief description or bio about the user.
     *
     * <p>Optional. Cannot exceed 500 characters.</p>
     */
    @Size(max = 500, message = "About section cannot exceed 500 characters", groups = GeneralValidationGroup.class)
    private String about;

    /**
     * The roles set of the user
     */
    @NotEmpty(message = "Role needs to be assigned to user", groups = CreateValidationGroup.class)
    private Set<Long> roleIds;
}
