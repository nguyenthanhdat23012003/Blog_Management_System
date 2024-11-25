package com.example.blog_app.models.dtos;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.GeneralValidationGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for handling category-related requests.
 *
 * <p>This class is used to capture and validate category input during operations
 * such as category creation and updates. It includes validation constraints
 * to ensure data integrity.</p>
 *
 * <p>Validation annotations are used to enforce rules, such as:</p>
 * <ul>
 *   <li>Title must not exceed 100 characters.</li>
 *   <li>Description is optional but cannot exceed 500 characters.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * CategoryRequestDto category = new CategoryRequestDto();
 * category.setTitle("Programming");
 * category.setDescription("Learn about programming languages and techniques.");
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryRequestDto {

    /**
     * The title of the category.
     *
     * <p>Must not exceed 100 characters.</p>
     */
    @NotEmpty(message = "Title is required", groups = CreateValidationGroup.class)
    @Size(max = 100, message = "Title must not exceed 100 characters", groups = GeneralValidationGroup.class)
    private String title;

    /**
     * The description of the category.
     *
     * <p>Optional. Cannot exceed 500 characters.</p>
     */
    @Size(max = 500, message = "Description must not exceed 500 characters", groups = GeneralValidationGroup.class)
    private String description;
}
