package com.example.blog_app.exceptions;

/**
 * Exception thrown when a requested resource cannot be found.
 *
 * <p>This exception extends {@link RuntimeException}, allowing it to be used
 * as an unchecked exception in the application. It is commonly used to indicate
 * that a requested entity does not exist in the database or other storage layer.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * Optional<User> user = userRepository.findById(userId);
 * if (user.isEmpty()) {
 *     throw new ResourceNotFoundException("User not found with ID: " + userId);
 * }
 * }
 * </pre>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
