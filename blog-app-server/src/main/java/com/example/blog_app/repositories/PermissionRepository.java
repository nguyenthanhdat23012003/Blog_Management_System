package com.example.blog_app.repositories;

import com.example.blog_app.models.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Permission} entities.
 *
 * <p>This interface provides CRUD operations and additional query methods
 * for interacting with the "permissions" table in the database. It extends
 * {@link JpaRepository}, which provides built-in methods for common
 * database operations.</p>
 *
 * <p>Custom methods can also be defined here to support specific queries.</p>
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * Finds a permission by its id.
     *
     * <p>This method retrieves a {@link Permission} entity that matches the given
     * id, if one exists.</p>
     *
     * @param id the id of the permission to search for.
     * @return an {@link Optional} containing the matching {@link Permission}, or an empty {@link Optional} if no permission is found.
     */
    Optional<Permission> findById(Long id);

    /**
     * Finds a permission by its name.
     *
     * <p>This method retrieves a {@link Permission} entity that matches the given
     * name, if one exists.</p>
     *
     * @param name the name of the permission to search for.
     * @return an {@link Optional} containing the matching {@link Permission}, or an empty {@link Optional} if no permission is found.
     */
    Optional<Permission> findByName(String name);
}
