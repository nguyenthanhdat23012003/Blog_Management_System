package com.example.blog_app.models.dtos;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.GeneralValidationGroup;
import com.example.blog_app.models.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for handling series-related requests.
 *
 * <p>This class is used to capture and validate series input during operations
 * such as series creation and updates. It includes validation constraints
 * to ensure data integrity.</p>
 *
 * <p>Validation annotations are used to enforce rules, such as:</p>
 * <ul>
 *   <li>Title must not exceed 100 characters.</li>
 *   <li>Description is optional but cannot exceed 500 characters.</li>
 *   <li>Author ID is mandatory and must not be null.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * SeriesRequestDto series = new SeriesRequestDto();
 * series.setTitle("Java Basics");
 * series.setDescription("A series of blogs introducing basic concepts in Java.");
 * series.setAuthorId(1L);
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
public class SeriesRequestDto {

    /**
     * The title of the series.
     *
     * <p>Must not exceed 100 characters.</p>
     */
    @NotEmpty(message = "Title is required", groups = CreateValidationGroup.class)
    @Size(max = 100, message = "Title must not exceed 100 characters", groups = GeneralValidationGroup.class)
    private String title;

    /**
     * The description of the series.
     *
     * <p>Optional. Cannot exceed 500 characters.</p>
     */
    @Size(max = 500, message = "Description must not exceed 500 characters", groups = GeneralValidationGroup.class)
    private String description;

    /**
     * The ID of the author who created the series.
     *
     * <p>This field is mandatory and references the {@link User} who owns the series.</p>
     */
    @NotNull(message = "Author ID is required", groups = CreateValidationGroup.class)
    private Long authorId;
}
