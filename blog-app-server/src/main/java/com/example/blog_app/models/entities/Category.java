package com.example.blog_app.models.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a category in the application.
 *
 * <p>This class maps to the "categories" table in the database and defines
 * the structure and relationships for managing categories.</p>
 *
 * <p>Each blog post has a title, content, and an associated author (a {@link User}).</p>
 */
@Entity
@Table(name = "categories")
@NoArgsConstructor
@Getter
@Setter
public class Category {

    /**
     * The unique identifier of the category.
     *
     * <p>Generated automatically using the {@link GenerationType#IDENTITY} strategy.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The title of the category.
     *
     * <p>This field cannot be null and has a maximum length of 100 characters.</p>
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * A detailed description of the category.
     *
     * <p>This field is optional and allows storing lengthy text as it uses
     * the `TEXT` type in the database, suitable for larger content.</p>
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Blogs associated with this category.
     * Inverse side of the many-to-many relationship, mapped by {@link Blog#categories}.
     * Uses a {@code Set} to avoid duplicate blogs.
     */
    @ManyToMany(mappedBy = "categories")
    private Set<Blog> blogs = new HashSet<>();
}
