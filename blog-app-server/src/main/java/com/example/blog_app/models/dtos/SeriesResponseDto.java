package com.example.blog_app.models.dtos;

import com.example.blog_app.common.util.DateTimeUtils;
import com.example.blog_app.models.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for handling series-related responses.
 *
 * <p>This class is used to provide series details in response payloads
 * for operations such as retrieving a single series or a list of series.</p>
 *
 * <p>It includes all the necessary fields to represent a series
 * and can be extended if additional metadata is required.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * SeriesResponseDto series = new SeriesResponseDto();
 * series.setId(1L);
 * series.setTitle("Java Basics");
 * series.setDescription("A series of blogs introducing basic concepts in Java.");
 * series.setAuthorId(1L);
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
public class SeriesResponseDto {

    /**
     * The unique identifier of the series.
     *
     * <p>Generated automatically when the series is created.</p>
     */
    private Long id;

    /**
     * The title of the series.
     *
     * <p>This field represents the name or label of the series.</p>
     */
    private String title;

    /**
     * The description of the series.
     *
     * <p>Provides additional details or context about the series.</p>
     */
    private String description;

    /**
     * The ID of the author who created the series.
     *
     * <p>References the {@link User} who owns the series.</p>
     */
    private Long authorId;

    /**
     * The timestamp when the series was created, stored as a {@link LocalDateTime}.
     *
     * <p>This field is ignored in the JSON response and is formatted into a string
     * using {@link #getCreatedAtFormatted()}.</p>
     */
    @JsonIgnore
    private LocalDateTime createdAt;

    /**
     * The timestamp when the series was last updated, stored as a {@link LocalDateTime}.
     *
     * <p>This field is ignored in the JSON response and is formatted into a string
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
