package com.example.blog_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for unauthorized actions.
 *
 * <p>This exception is thrown when a user attempts to perform an action
 * that they are not authorized to execute. It is annotated with
 * {@link ResponseStatus} to automatically map to an HTTP 401 Unauthorized response.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * if (!isAuthorized(user)) {
 *     throw new UnauthorizedException("User does not have permission to perform this action.");
 * }
 * </pre>
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructs a new {@code UnauthorizedException} with the specified detail message.
     *
     * @param message the detail message, providing additional context for the exception
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
