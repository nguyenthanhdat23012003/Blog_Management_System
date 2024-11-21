package com.example.blog_app.repositories;

import com.example.blog_app.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Category} entities.
 *
 * <p>This interface provides CRUD operations and additional query methods
 * for interacting with the "categories" table in the database. It extends
 * {@link JpaRepository}, which provides built-in methods for common
 * database operations.</p>
 *
 * <p>Custom methods can also be defined here to support specific queries.</p>
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /**
     * Finds a category by its ID.
     *
     * <p>This method retrieves a {@link Category} entity that matches the given
     * ID, if one exists.</p>
     *
     * @param id the ID of the category to search for.
     * @return an {@link Optional} containing the matching {@link Category}, or an empty {@link Optional} if no category is found.
     */
    Optional<Category> findById(Long id);
}
