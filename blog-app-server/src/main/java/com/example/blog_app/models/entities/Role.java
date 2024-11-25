package com.example.blog_app.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a role in the application.
 *
 * <p>This class maps to the "roles" table in the database and defines the structure
 * and relationships for managing roles.</p>
 *
 * <p>Roles are used to group permissions and assign them to users for access control.</p>
 *
 * <p>Examples: "ADMIN", "USER", "MODERATOR".</p>
 */
@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role {

    /**
     * The unique identifier of the role.
     *
     * <p>Generated automatically using the {@link GenerationType#IDENTITY} strategy.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the role.
     *
     * <p>This field must be unique and cannot be null.</p>
     * <p>Examples: "ADMIN", "USER".</p>
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * The users associated with this role.
     *
     * <p>Defines a many-to-many relationship with the {@link User} entity,
     * where roles can be assigned to multiple users, and users can have multiple roles.</p>
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    /**
     * The permissions associated with this role.
     *
     * <p>Defines a many-to-many relationship with the {@link Permission} entity,
     * using the join table "role_permissions".</p>
     * <p>This allows roles to be linked to multiple permissions, enabling
     * fine-grained access control.</p>
     */
    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    /**
     * Indicates whether the role is immutable.
     *
     * <p>If {@code true}, this role cannot be modified or deleted.</p>
     * <p>Defaults to {@code false} in the database.</p>
     */
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean immutable;
}
