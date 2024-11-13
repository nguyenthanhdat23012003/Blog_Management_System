package com.example.blog_app.exceptions;

public class ImmutableResourceException extends RuntimeException {
    public ImmutableResourceException(String message) {
        super(message);
    }
}
