package com.example.blog_app.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a blog post in the application.
 *
 * <p>This class maps to the "blogs" table in the database and defines
 * the structure and relationships for managing blog posts.</p>
 *
 * <p>Each blog post has a title, content, and an associated author (a {@link User}).</p>
 */
@Entity
@Table(name = "blogs")
@NoArgsConstructor
@Getter
@Setter
public class Blog {

    /**
     * The unique identifier of the blog post.
     *
     * <p>Generated automatically using the {@link GenerationType#IDENTITY} strategy.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the blog post.
     *
     * <p>This field cannot be null and has a maximum length of 100 characters.</p>
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * The content of the blog post.
     *
     * <p>Stored as a JSON string in the database.</p>
     */
    @Column(name = "content", columnDefinition = "JSON")
    private String content;

    /**
     * The author of the blog post.
     *
     * <p>Defines a many-to-one relationship with the {@link User} entity.</p>
     * <p>This field cannot be null and references the author's ID.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    /**
     * Categories associated with this blog.
     * Managed via the join table `blog_categories` with `blog_id` and `category_id`.
     * Uses a {@code Set} to avoid duplicate categories.
     */
    @ManyToMany
    @JoinTable(
            name = "blog_categories",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    /**
     * The series this blog belongs to.
     *
     * <p>This field defines a many-to-one relationship with {@link Series},
     * allowing each blog to optionally belong to a single series.
     * The relationship is not mandatory, as a blog can exist without being part of a series.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id") // Nullable foreign key to represent optional series association
    private Series series;

    /**
     * The timestamp when the blog post was created.
     *
     * <p>Automatically set when the blog post is created and cannot be updated.</p>
     * <p>Formatted as "yyyy-MM-dd HH:mm:ss" in JSON responses.</p>
     */
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the blog post was last updated.
     *
     * <p>Automatically updated whenever the blog post is modified.</p>
     * <p>Formatted as "yyyy-MM-dd HH:mm:ss" in JSON responses.</p>
     */
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
