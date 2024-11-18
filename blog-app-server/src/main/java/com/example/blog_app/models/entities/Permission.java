package com.example.blog_app.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a permission in the application.
 *
 * <p>This class maps to the "permissions" table in the database and
 * defines the structure and relationships for managing permissions.</p>
 *
 * <p>Permissions are used to control access to specific functionality
 * or resources within the application.</p>
 *
 * <p>Example: "READ_PRIVILEGES", "WRITE_PRIVILEGES"</p>
 */
@Entity
@Table(name = "permissions")
@NoArgsConstructor
@Getter
@Setter
public class Permission {

    /**
     * The unique identifier of the permission.
     *
     * <p>Generated automatically using the {@link GenerationType#IDENTITY} strategy.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the permission.
     *
     * <p>This field must be unique and cannot be null.</p>
     * <p>Example: "READ_PRIVILEGES", "WRITE_PRIVILEGES".</p>
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * The roles associated with this permission.
     *
     * <p>Defines a many-to-many relationship with the {@link Role} entity,
     * where roles can have multiple permissions, and permissions can be
     * assigned to multiple roles.</p>
     */
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    /**
     * Indicates whether the permission is immutable.
     *
     * <p>If {@code true}, this permission cannot be modified or deleted.</p>
     * <p>Defaults to {@code false} in the database.</p>
     */
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean immutable;
}
