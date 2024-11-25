package com.example.blog_app.common.validation;

/**
 * Marker interface used for grouping validation constraints
 * that are applicable to multiple operations, such as both
 * entity creation and update.
 *
 * <p>This interface acts as a general validation group, providing
 * a base for more specific groups like {@code CreateValidationGroup}
 * and {@code UpdateValidationGroup}. Constraints assigned to this group
 * will be validated across all operations using these derived groups.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @NotEmpty(groups = GeneralValidationGroup.class)
 * private String fieldName;
 * }
 * </pre>
 *
 * @see CreateValidationGroup
 * @see UpdateValidationGroup
 * @see jakarta.validation.groups.Default
 */
public interface GeneralValidationGroup {
}
