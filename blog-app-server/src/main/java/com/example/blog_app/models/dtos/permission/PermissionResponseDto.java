package com.example.blog_app.models.dtos.permission;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing permissions in responses.
 *
 * <p>This class encapsulates the data returned to clients for permissions,
 * such as the permission's ID and name.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * PermissionResponseDto response = new PermissionResponseDto();
 * response.setId(1L);
 * response.setName("READ_PRIVILEGES");
 * }
 * </pre>
 */
@NoArgsConstructor
@Getter
@Setter
public class PermissionResponseDto {

    /**
     * The unique identifier of the permission.
     */
    private Long id;

    /**
     * The name of the permission.
     *
     * <p>This field is unique and represents the permission's name.</p>
     * <p>Example: "READ_PRIVILEGES", "WRITE_PRIVILEGES".</p>
     */
    private String name;
}
