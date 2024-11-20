package com.example.blog_app.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a series in the application.
 *
 * <p>This class maps to the "series" table in the database and defines
 * the structure and relationships for managing series.</p>
 *
 * <p>Each blog post has a title, content, and an associated author (a {@link User}).</p>
 */
@Entity
@Table(name = "series")
@NoArgsConstructor
@Getter
@Setter
public class Series {

    /**
     * The unique identifier of the series.
     *
     * <p>Generated automatically using the {@link GenerationType#IDENTITY} strategy.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The title of the series.
     *
     * <p>This field cannot be null and has a maximum length of 100 characters.</p>
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * A detailed description of the series.
     *
     * <p>This field is optional and allows storing lengthy text as it uses
     * the `TEXT` type in the database, suitable for larger content.</p>
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Blogs associated with this series.
     *
     * <p>This field defines the inverse side of a one-to-many relationship
     * with {@link Blog}. A series can have zero or more blogs, and each blog
     * optionally references a series. Blogs are not automatically managed
     * (no cascade or orphan removal), ensuring their independence.</p>
     */
    @OneToMany(mappedBy = "series")
    private Set<Blog> blogs = new HashSet<>();

    /**
     * The author of the series.
     *
     * <p>Defines a many-to-one relationship with the {@link User} entity.</p>
     * <p>This field cannot be null and references the author's ID.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User user;
}
