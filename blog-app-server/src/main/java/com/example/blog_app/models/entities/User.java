package com.example.blog_app.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity representing a user in the application.
 *
 * <p>This class maps to the "users" table in the database and defines the
 * structure and relationships for managing user data.</p>
 *
 * <p>Users are associated with roles to define their permissions and access levels.</p>
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    /**
     * The unique identifier of the user.
     *
     * <p>Generated automatically using the {@link GenerationType#IDENTITY} strategy.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The name of the user.
     *
     * <p>This field cannot be null and has a maximum length of 100 characters.</p>
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * The email address of the user.
     *
     * <p>This field must be unique and cannot be null.</p>
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * The hashed password for the user account.
     *
     * <p>This field cannot be null and is stored securely as a hashed value.</p>
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * A brief description or bio about the user.
     *
     * <p>Optional. Cannot exceed 500 characters.</p>
     */
    @Column(name = "about", length = 500)
    private String about;

    /**
     * The roles associated with the user.
     *
     * <p>Defines a many-to-many relationship with the {@link Role} entity.</p>
     * <p>Roles determine the permissions and access levels of the user.</p>
     */
    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Indicates whether the user is immutable.
     *
     * <p>If {@code true}, this user cannot be modified or deleted.</p>
     * <p>Defaults to {@code false} in the database.</p>
     */
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean immutable;

    /**
     * The timestamp when the user was created.
     *
     * <p>Automatically set when the user is created and cannot be updated.</p>
     * <p>Formatted as "yyyy-MM-dd HH:mm:ss" in JSON responses.</p>
     */
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the user was last updated.
     *
     * <p>Automatically updated whenever the user is modified.</p>
     * <p>Formatted as "yyyy-MM-dd HH:mm:ss" in JSON responses.</p>
     */
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * List of blogs authored by the user.
     *
     * <p>Mapped by {@link Blog#user}, with cascading CRUD operations and orphan removal enabled.</p>
     * <p>Uses LAZY loading to fetch blogs only when accessed.</p>
     */
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Blog> blogs = new ArrayList<>();
}
