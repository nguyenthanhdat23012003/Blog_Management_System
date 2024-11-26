package com.example.blog_app.models.dtos.user;

import com.example.blog_app.common.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data Transfer Object (DTO) for returning user information in API responses.
 *
 * <p>This class provides user details such as ID, name, email, and about information.
 * It also formats creation and update timestamps to a human-readable string format
 * using {@link DateTimeUtils}.</p>
 *
 * <p>Example JSON response:</p>
 * <pre>
 * {
 *   "id": 1,
 *   "name": "John Doe",
 *   "email": "john.doe@example.com",
 *   "about": "A software engineer.",
 *   "create_at": "Sun, Nov 19 2024 14:23:45 UTC",
 *   "update_at": "Mon, Nov 20 2024 16:15:30 UTC"
 * }
 * </pre>
 */
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {

    /**
     * The unique identifier of the user.
     */
    private int id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * A brief description or bio about the user.
     */
    private String about;

    /**
     * The roles set of the user
     */
    private Set<Long> roleIds;

    /**
     * The timestamp when the user was created, stored as a {@link LocalDateTime}.
     *
     * <p>This field is ignored in the JSON response and is formatted into a string
     * using {@link #getCreatedAtFormatted()}.</p>
     */
    @JsonIgnore
    private LocalDateTime createdAt;

    /**
     * The timestamp when the user was last updated, stored as a {@link LocalDateTime}.
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
