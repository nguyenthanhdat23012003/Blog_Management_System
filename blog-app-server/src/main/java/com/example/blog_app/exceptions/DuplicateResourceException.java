package com.example.blog_app.exceptions;

/**
 * Exception thrown when attempting to create or add a resource
 * that already exists in the system.
 *
 * <p>This exception extends {@link RuntimeException}, allowing
 * it to be used for unchecked exceptions in the application.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * if (repository.existsByEmail(email)) {
 *     throw new DuplicateResourceException("Email already exists: " + email);
 * }
 * }
 * </pre>
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new {@code DuplicateResourceException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
