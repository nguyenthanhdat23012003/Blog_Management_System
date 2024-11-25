package com.example.blog_app.common.validation;

/**
 * Marker interface used for grouping validation constraints
 * applicable during the update of an entity.
 *
 * <p>This interface is typically used in conjunction with
 * annotations such as {@code @Validated} or {@code @Valid}
 * to apply different validation rules for update operations.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @NotEmpty(groups = UpdateValidationGroup.class)
 * private String name;
 * }
 * </pre>
 *
 * @see CreateValidationGroup
 * @see jakarta.validation.groups.Default
 */
public interface UpdateValidationGroup extends GeneralValidationGroup {
}
