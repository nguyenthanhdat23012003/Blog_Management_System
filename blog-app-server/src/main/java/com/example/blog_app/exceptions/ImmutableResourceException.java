package com.example.blog_app.exceptions;

/**
 * Exception thrown when attempting to modify a resource that is immutable.
 *
 * <p>This exception extends {@link RuntimeException}, allowing it to be used
 * for unchecked exceptions in the application.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * if (user.isImmutable()) {
 *     throw new ImmutableResourceException("This resource cannot be modified: " + resourceId);
 * }
 * }
 * </pre>
 */
public class ImmutableResourceException extends RuntimeException {

    /**
     * Constructs a new {@code ImmutableResourceException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ImmutableResourceException(String message) {
        super(message);
    }
}
