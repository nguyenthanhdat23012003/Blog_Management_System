package com.example.blog_app.models.dtos;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.GeneralValidationGroup;
import com.example.blog_app.models.entities.Series;
import com.example.blog_app.models.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

/**
 * Data Transfer Object (DTO) for handling blog-related requests.
 *
 * <p>This class is used to capture and validate blog input during operations
 * such as blog creation and updates. It includes validation constraints
 * to ensure data integrity.</p>
 *
 * <p>Validation annotations are used to enforce rules, such as:</p>
 * <ul>
 *   <li>Title must not exceed 100 characters.</li>
 *   <li>Content must be non-empty.</li>
 *   <li>Author ID is mandatory and must not be null.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * BlogRequestDto blog = new BlogRequestDto();
 * blog.setTitle("Introduction to Spring Boot");
 * blog.setContent("{\"body\":\"Content of the blog\"}");
 * blog.setAuthorId(1L);
 * blog.setCategoryIds(Set.of(1, 2));
 * blog.setSeriesId(1L);
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
public class BlogRequestDto {

    /**
     * The title of the blog post.
     *
     * <p>Must not exceed 100 characters.</p>
     */
    @NotEmpty(message = "Title is required", groups = CreateValidationGroup.class)
    @Size(max = 100, message = "Title must not exceed 100 characters", groups = GeneralValidationGroup.class)
    private String title;

    /**
     * The content of the blog post.
     *
     * <p>Must not be empty. Stored as a JSON string in the database.</p>
     */
    @NotEmpty(message = "Content is required", groups = CreateValidationGroup.class)
    private Map<String, Object> content;

    /**
     * The ID of the author who created the blog post.
     *
     * <p>This field is mandatory and references the {@link User} who owns the blog.</p>
     */
    @NotNull(message = "Author ID is required", groups = CreateValidationGroup.class)
    private Long authorId;

    /**
     * The IDs of the categories associated with this blog.
     *
     * <p>Optional. Used to link the blog to one or more categories.</p>
     */
    private Set<Long> categoryIds;

    /**
     * The ID of the series this blog belongs to.
     *
     * <p>Optional. Represents the relationship with a {@link Series}.</p>
     */
    private Long seriesId;
}
