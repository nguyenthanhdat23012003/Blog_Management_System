package com.example.blog_app.models.dtos;

import com.example.blog_app.common.util.DateTimeUtils;
import com.example.blog_app.models.entities.Series;
import com.example.blog_app.models.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Data Transfer Object (DTO) for handling blog-related responses.
 *
 * <p>This class is used to provide blog details in response payloads
 * for operations such as retrieving a single blog or a list of blogs.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * BlogResponseDto blog = new BlogResponseDto();
 * blog.setId(1L);
 * blog.setTitle("Introduction to Spring Boot");
 * blog.setContent("{\"body\":\"Content of the blog\"}");
 * blog.setAuthorId(1L);
 * blog.setCategoryIds(Set.of(1L, 2L));
 * blog.setSeriesId(1L);
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
public class BlogResponseDto {

    /**
     * The unique identifier of the blog post.
     *
     * <p>Generated automatically when the blog post is created.</p>
     */
    private Long id;

    /**
     * The title of the blog post.
     *
     * <p>This field represents the name or label of the blog.</p>
     */
    private String title;

    /**
     * The content of the blog post.
     *
     * <p>Stored as a JSON string.</p>
     */
    private Map<String, Object> content;

    /**
     * The ID of the author who created the blog post.
     *
     * <p>References the {@link User} who owns the blog.</p>
     */
    private Long authorId;

    /**
     * The IDs of the categories associated with this blog.
     *
     * <p>Represents the relationship with one or more categories.</p>
     */
    private Set<Long> categoryIds;

    /**
     * The ID of the series this blog belongs to.
     *
     * <p>Represents the relationship with a {@link Series}.</p>
     */
    private Long seriesId;

    /**
     * The timestamp when the blog post was created.
     *
     * <p>Stored as a {@link LocalDateTime} and formatted into a string
     * using {@link #getCreatedAtFormatted()}.</p>
     */
    @JsonIgnore
    private LocalDateTime createdAt;

    /**
     * The timestamp when the blog post was last updated.
     *
     * <p>Stored as a {@link LocalDateTime} and formatted into a string
     * using {@link #getUpdatedAtFormatted()}.</p>
     */
    @JsonIgnore
    private LocalDateTime updatedAt;

    /**
     * Returns the formatted creation timestamp as a string.
     *
     * @return the formatted creation timestamp, or {@code null} if {@code createdAt} is not set
     */
    @JsonProperty("create_at")
    public String getCreatedAtFormatted() {
        return DateTimeUtils.format(createdAt);
    }

    /**
     * Returns the formatted update timestamp as a string.
     *
     * @return the formatted update timestamp, or {@code null} if {@code updatedAt} is not set
     */
    @JsonProperty("update_at")
    public String getUpdatedAtFormatted() {
        return DateTimeUtils.format(updatedAt);
    }
}
