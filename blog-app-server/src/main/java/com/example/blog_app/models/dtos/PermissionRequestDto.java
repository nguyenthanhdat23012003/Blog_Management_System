package com.example.blog_app.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for creating or updating permissions.
 *
 * <p>This class encapsulates the data required for permission creation
 * or updates, such as the permission's name.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * PermissionRequestDto request = new PermissionRequestDto();
 * request.setName("READ_PRIVILEGES");
 * }
 * </pre>
 */
@NoArgsConstructor
@Getter
@Setter
public class PermissionRequestDto {

    /**
     * The name of the permission.
     *
     * <p>This field must be unique and cannot be null.</p>
     * <p>Example: "READ_PRIVILEGES", "WRITE_PRIVILEGES".</p>
     */
    private String name;
}
