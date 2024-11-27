package com.example.blog_app.exceptions;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for managing and responding to exceptions
 * across the entire application.
 *
 * <p>This class provides methods to handle various exceptions
 * such as {@link ResourceNotFoundException}, {@link DuplicateResourceException},
 * {@link ImmutableResourceException}, and validation errors.</p>
 *
 * <p>Each exception handler constructs a meaningful response
 * with an appropriate HTTP status code and error details.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link UnauthorizedException} exceptions.
     *
     * @param ex the exception to handle
     * @return a response entity containing error details with HTTP status 401 (UNAUTHORIZED)
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Unauthorized");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles {@link ResourceNotFoundException} exceptions.
     *
     * @param ex the exception to handle
     * @return a response entity containing error details with HTTP status 404 (NOT_FOUND)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Resource Not Found");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles {@link DuplicateResourceException} exceptions.
     *
     * @param ex the exception to handle
     * @return a response entity containing error details with HTTP status 409 (CONFLICT)
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateResourceException(DuplicateResourceException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Duplicate Resource");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles {@link ImmutableResourceException} exceptions.
     *
     * @param ex the exception to handle
     * @return a response entity containing error details with HTTP status 403 (FORBIDDEN)
     */
    @ExceptionHandler(ImmutableResourceException.class)
    public ResponseEntity<Map<String, String>> handleImmutableResourceException(ImmutableResourceException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Immutable Resource");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Handles generic {@link Exception} instances not covered by other handlers.
     *
     * @param ex the exception to handle
     * @return a response entity containing error details with HTTP status 500 (INTERNAL_SERVER_ERROR)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Handles validation exceptions thrown when method arguments fail validation.
     *
     * @param ex the exception containing validation errors
     * @return a response entity containing a list of validation error details with HTTP status 400 (BAD_REQUEST)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    Map<String, String> fieldError = new HashMap<>();
                    fieldError.put("field", error.getField());
                    fieldError.put("message", error.getDefaultMessage());
                    return fieldError;
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles {@link HttpMessageNotReadableException} to detect and respond to unrecognized fields.
     *
     * @param ex the exception to handle
     * @return a response entity containing error details with HTTP status 400 (BAD_REQUEST)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        Map<String, Object> response = new HashMap<>();

        if (cause instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException unrecognizedEx = (UnrecognizedPropertyException) cause;
            response.put("error", "Validation Error");
            response.put("message", "Unrecognized field: " + unrecognizedEx.getPropertyName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
