package com.example.blog_app.repositories;

import com.example.blog_app.models.entities.Series;
import com.example.blog_app.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Series} entities.
 *
 * <p>This interface provides CRUD operations and additional query methods
 * for interacting with the "series" table in the database. It extends
 * {@link JpaRepository}, which provides built-in methods for common
 * database operations.</p>
 *
 * <p>Custom methods can also be defined here to support specific queries.</p>
 */
@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {

    /**
     * Finds a series by its ID.
     *
     * <p>This method retrieves a {@link Series} entity that matches the given
     * ID, if one exists.</p>
     *
     * @param id the ID of the series to search for.
     * @return an {@link Optional} containing the matching {@link Series}, or an empty {@link Optional} if no series is found.
     */
    Optional<Series> findById(Long id);

    /**
     * Retrieves all series authored by a specific user.
     *
     * @param user the user whose series are to be retrieved
     * @return a list of series authored by the user
     */
    List<Series> findByUser(User user);
}
