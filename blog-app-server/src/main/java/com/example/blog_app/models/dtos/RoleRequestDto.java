package com.example.blog_app.models.dtos;

import com.example.blog_app.common.validation.CreateValidationGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for role creation or update requests.
 *
 * <p>This class is used to encapsulate data required to create or update a role.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * RoleRequestDto request = new RoleRequestDto();
 * request.setName("MODERATOR");
 * request.setPermissionIds(Set.of(1L, 2L));
 * }
 * </pre>
 */
@NoArgsConstructor
@Getter
@Setter
public class RoleRequestDto {

    /**
     * The name of the role.
     *
     * <p>This field is required and must be unique.</p>
     * <p>Examples: "ADMIN", "USER", "MODERATOR".</p>
     */
    @NotEmpty(message = "Name cannot be empty", groups = CreateValidationGroup.class)
    private String name;

    /**
     * A set of permission IDs to be associated with the role.
     *
     * <p>This allows specifying which permissions should be assigned to this role
     * during creation or update.</p>
     */
    @NotEmpty(message = "Permission cannot be empty", groups = CreateValidationGroup.class)
    private Set<Long> permissionIds;
}
