package com.example.blog_app.repositories;

import com.example.blog_app.models.entities.Blog;
import com.example.blog_app.models.entities.Category;
import com.example.blog_app.models.entities.Series;
import com.example.blog_app.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Blog} entities.
 *
 * <p>This interface provides CRUD operations and additional query methods
 * for interacting with the "blogs" table in the database. It extends
 * {@link JpaRepository}, which provides built-in methods for common
 * database operations.</p>
 *
 * <p>Custom methods can also be defined here to support specific queries.</p>
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * Finds a blog by its ID.
     *
     * <p>This method retrieves a {@link Blog} entity that matches the given
     * ID, if one exists.</p>
     *
     * @param id the ID of the blog to search for.
     * @return an {@link Optional} containing the matching {@link Blog}, or an empty {@link Optional} if no blog is found.
     */
    Optional<Blog> findById(Long id);
    List<Blog> findByUser(User user);
    List<Blog> findBySeries(Series series);
    List<Blog> findByCategories(Category category);
}
