package com.example.blog_app.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing roles.
 *
 * <p>This class is used to transfer role data between different
 * layers of the application, such as controllers and services.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * RoleDto role = new RoleDto();
 * role.setName("ADMIN");
 * }
 * </pre>
 */
@NoArgsConstructor
@Getter
@Setter
public class RoleDto {

    /**
     * The name of the role.
     *
     * <p>Examples: "ADMIN", "USER", "MODERATOR".</p>
     */
    private String name;
}
