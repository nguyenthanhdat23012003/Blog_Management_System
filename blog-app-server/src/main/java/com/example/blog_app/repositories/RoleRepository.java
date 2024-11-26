package com.example.blog_app.repositories;

import com.example.blog_app.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Role} entities.
 *
 * <p>This interface provides CRUD operations and additional query methods
 * for interacting with the "roles" table in the database. It extends
 * {@link JpaRepository}, which provides built-in methods for common
 * database operations.</p>
 *
 * <p>Custom methods can also be defined here to support specific queries.</p>
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its name.
     *
     * <p>This method retrieves a {@link Role} entity that matches the given
     * name, if one exists.</p>
     *
     * @param name the name of the role to search for.
     * @return an {@link Optional} containing the matching {@link Role}, or an empty {@link Optional} if no role is found.
     */
    Optional<Role> findByName(String name);

    /**
     * Finds a role by its id.
     *
     * <p>This method retrieves a {@link Role} entity that matches the given
     * id, if one exists.</p>
     *
     * @param roleId the id of the role to search for.
     * @return an {@link Optional} containing the matching {@link Role}, or an empty {@link Optional} if no role is found.
     */
    Optional<Role> findById(Long roleId);
}
