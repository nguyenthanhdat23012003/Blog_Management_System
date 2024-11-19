package com.example.blog_app.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing permissions.
 *
 * <p>This class is used to transfer permission data between
 * different layers of the application, such as controllers and services.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * PermissionDto permission = new PermissionDto();
 * permission.setName("READ_PRIVILEGES");
 * }
 * </pre>
 */
@NoArgsConstructor
@Getter
@Setter
public class PermissionDto {

    /**
     * The name of the permission.
     */
    private String name;
}
