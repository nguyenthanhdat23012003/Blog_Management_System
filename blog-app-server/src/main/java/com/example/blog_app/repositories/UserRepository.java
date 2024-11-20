package com.example.blog_app.repositories;

import com.example.blog_app.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 *
 * <p>This interface provides CRUD operations and additional query methods
 * for interacting with the "users" table in the database. It extends
 * {@link JpaRepository}, which provides built-in methods for common
 * database operations.</p>
 *
 * <p>Custom methods can also be defined here to support specific queries.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * <p>This method retrieves a {@link User} entity that matches the given
     * email address, if one exists.</p>
     *
     * @param email the email address to search for.
     * @return an {@link Optional} containing the matching {@link User}, or an empty {@link Optional} if no user is found.
     */
    Optional<User> findByEmail(String email);
}
