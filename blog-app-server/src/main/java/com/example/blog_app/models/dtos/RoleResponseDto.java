package com.example.blog_app.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for representing roles in responses.
 *
 * <p>This class is used to send role data from the application to clients.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * RoleResponseDto response = new RoleResponseDto();
 * response.setId(1L);
 * response.setName("ADMIN");
 * response.setPermissionIds(Set.of(1L, 2L, 3L));
 * }
 * </pre>
 */
@NoArgsConstructor
@Getter
@Setter
public class RoleResponseDto {

    /**
     * The unique identifier of the role.
     */
    private Long id;

    /**
     * The name of the role.
     *
     * <p>Examples: "ADMIN", "USER", "MODERATOR".</p>
     */
    private String name;

    /**
     * A set of permission IDs associated with the role.
     *
     * <p>This field provides a reference to all permissions assigned to this role.</p>
     */
    private Set<Long> permissionIds;
}
